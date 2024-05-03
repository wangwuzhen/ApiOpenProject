package com.ice.icegateway;

import com.ice.iceclientsdk.utils.SignUtils;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 全局拦截过滤器
 * 每次拦截请求 都会经过这里
 */
@Slf4j
@Component
public class CustomGlobalFilter implements GlobalFilter, Ordered {

    private static final List<String> IP_WHITE_LIST= Arrays.asList("127.0.0.1");
    /**
     * exchange 路由交换机  我们所有的请求的信息、响应的信息、响应体、请求体都能从这里拿到
     * chain责任链模式 因为我们的所有过滤器是按照从上到下的顺序依次执行，形成了一个链条。
     * 所以这里用了一个chain，如果当前过滤器对请求进行了过滤后发现可以放行，
     * 就要调用责任链中的next方法，相当于直接找到下一个过滤器，这里称为filter。
     * 有时候我们需要在责任链中使用 next，而在这里它使用了 filter 来找到下一个过滤器，从而正常地放行请求。
     * @param exchange
     * @param chain
     * @return
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        //1.请求日志
        ServerHttpRequest request = exchange.getRequest();
        log.info("请求唯一标识："+request.getId());
        log.info("请求路径："+request.getPath());
        log.info("请求方法："+request.getMethod());
        log.info("请求参数："+request.getQueryParams());

       String sourceAddress= request.getLocalAddress().getHostString();
       log.info("请求来源地址："+sourceAddress);
       log.info("请求来源地址:"+request.getRemoteAddress());
        //拿到响应对象
        ServerHttpResponse response = exchange.getResponse();
        //（黑白名单）  放行地址里不包含来源地址 禁止通行
        if (!IP_WHITE_LIST.contains(sourceAddress)){
            response.setStatusCode(HttpStatus.FORBIDDEN);
            return response.setComplete();
        }
        //用户鉴权（判断ak、sk是否合法）
        HttpHeaders headers = request.getHeaders();
        String accessKey = headers.getFirst("accessKey");
        String nonce = headers.getFirst("nonce");
        String timestamp = headers.getFirst("timestamp");
        String body = headers.getFirst("body");
        String sign=headers.getFirst("sign");
        //todo 实际是从数据库读取
        if(!"ice".equals(accessKey)){
           return handleNoAuth(response);
        }
        if (Long.parseLong(nonce)>10000) {
            return handleNoAuth(response);
        }
        //todo 时间和当前时间 不能超过5分钟
        long currentTimeMillis = System.currentTimeMillis()/1000;
        final Long FIVE_MINUTES=60*5L;
        if (currentTimeMillis-Long.parseLong(timestamp)>=FIVE_MINUTES){
            return handleNoAuth(response);
        }
//        if (timestamp){
//        }
        //todo 实际是从数据库中去查
        String serverSign = SignUtils.genSign(body, "abcdefg");
        if (!sign.equals(serverSign)){
            throw new  RuntimeException("无权限");
        }

        //请求的模拟接口是否存在(从数据库中查询模拟接口是否存在) todo


        //请求转发，调用模拟接口
        Mono<Void>filter=chain.filter(exchange);
        log.info("响应："+response.getStatusCode());
        //响应日志

        return handleResponse(exchange,chain);

//
//        log.info("custom global filter");
//        return filter;
    }
    public Mono<Void> handleResponse(ServerWebExchange exchange, GatewayFilterChain chain) {

        try {
            // 获取原始应对象
            ServerHttpResponse originalResponse = exchange.getResponse();
            // 获取数据缓冲工厂
            DataBufferFactory bufferFactory = originalResponse.bufferFactory();

            // 获取响应的状态码
            HttpStatus statusCode = originalResponse.getStatusCode();

            // 判断状态码是否为200 OK(按道理,现在没有调用,是拿不到响应码的,对这个保持怀疑 沉思.jpg)
            if(statusCode == HttpStatus.OK) {
                // 创建一个装饰后的响应对象(开始穿装备，增强能力)
                ServerHttpResponseDecorator decoratedResponse = new ServerHttpResponseDecorator(originalResponse) {

                    // 重写writeWith方法，用于处理响应体的数据
                    // 这段方法就是只要当我们的模拟接口调用完成之后,等它返回结果，
                    // 就会调用writeWith方法,我们就能根据响应结果做一些自己的处理
                    @Override
                    public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                        log.info("body instanceof Flux: {}", (body instanceof Flux));
                        // 判断响应体是否是Flux类型
                        if (body instanceof Flux) {
                            Flux<? extends DataBuffer> fluxBody = Flux.from(body);
                            // 返回一个处理后的响应体
                            // (这里就理解为它在拼接字符串,它把缓冲区的数据取出来，一点一点拼接好)
                            return super.writeWith(fluxBody.map(dataBuffer -> {
                                //调用成功，接口调用次数+1
                                // 读取响应体的内容并转换为字节数组
                                byte[] content = new byte[dataBuffer.readableByteCount()];
                                dataBuffer.read(content);
                                DataBufferUtils.release(dataBuffer);//释放掉内存
                                // 构建日志
                                StringBuilder sb2 = new StringBuilder(200);

                                List<Object> rspArgs = new ArrayList<>();
                                rspArgs.add(originalResponse.getStatusCode());
                                //rspArgs.add(requestUrl);
                                String data = new String(content, StandardCharsets.UTF_8);//data


                                //打印日志
                                sb2.append(data);
                                log.info("响应结果："+data);
                                // 将处理后的内容重新包装成DataBuffer并返回
                                return bufferFactory.wrap(content);
                            }));
                        } else {
                            //调用失败，返回一个规范的错误码
                            log.error("<--- {} 响应code异常", getStatusCode());
                        }
                        return super.writeWith(body);
                    }
                };
                // 对于200 OK的请求,将装饰后的响应对象传递给下一个过滤器链,并继续处理(设置repsonse对象为装饰过的)
                return chain.filter(exchange.mutate().response(decoratedResponse).build());
            }
            // 对于非200 OK的请求，直接返回，进行降级处理
            return chain.filter(exchange);
        }catch (Exception e){
            // 处理异常情况，记录错误日志
            log.error("gateway log exception.\n" + e);
            return chain.filter(exchange);
        }
    }

    @Override
    public int getOrder() {
        return -1;
    }
    public Mono<Void> handleNoAuth(ServerHttpResponse response){
        response.setStatusCode(HttpStatus.FORBIDDEN);
        return  response.setComplete();
    }
    public Mono<Void> handleInvokeError(ServerHttpResponse response){
        response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
        return  response.setComplete();
    }
}