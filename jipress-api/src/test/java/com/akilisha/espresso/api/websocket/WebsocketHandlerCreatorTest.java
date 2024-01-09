package com.akilisha.espresso.api.websocket;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class WebsocketHandlerCreatorTest {

    WebsocketHandlerCreator<BigDecimal> creator = new WebsocketHandlerCreator<>() {
        @Override
        public BigDecimal getSession() {
            return null;
        }

        @Override
        public boolean isConnected() {
            return false;
        }

        @Override
        public boolean isNotConnected() {
            return false;
        }

        @Override
        public Object getRemote() {
            return null;
        }

        @Override
        public IWebsocketHandler<BigDecimal> build() {
            return mock(IWebsocketHandler.class);
        }
    };

    @Test
    void onConnect() {
        OnConnect<BigDecimal> handler = mock(OnConnect.class);
        creator.onConnect(handler);
        creator.onConnectHandler.accept(new BigDecimal(10));
        ArgumentCaptor<BigDecimal> number = ArgumentCaptor.forClass(BigDecimal.class);
        verify(handler, times(1)).accept(number.capture());
        assertThat(number.getValue().intValue()).isEqualTo(10);
    }

    @Test
    void onError() {
        OnError handler = mock(OnError.class);
        creator.onError(handler);
        creator.onErrorHandler.accept(new Exception("Hit it"));
        ArgumentCaptor<Throwable> cause = ArgumentCaptor.forClass(Throwable.class);
        verify(handler, times(1)).accept(cause.capture());
        assertThat(cause.getValue().getMessage()).isEqualTo("Hit it");
    }

    @Test
    void onMessage() {
        OnMessage handler = mock(OnMessage.class);
        creator.onMessage(handler);
        creator.onMessageHandler.accept("Roll with it");
        ArgumentCaptor<String> message = ArgumentCaptor.forClass(String.class);
        verify(handler, times(1)).accept(message.capture());
        assertThat(message.getValue()).isEqualTo("Roll with it");
    }

    @Test
    void onBinary() {
        OnBinary handler = mock(OnBinary.class);
        creator.onBinary(handler);
        creator.onBinaryHandler.accept("Roll with it".getBytes(), 0, 12);
        ArgumentCaptor<byte[]> payload = ArgumentCaptor.forClass(byte[].class);
        ArgumentCaptor<Integer> offset = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<Integer> length = ArgumentCaptor.forClass(Integer.class);
        verify(handler, times(1)).accept(payload.capture(), offset.capture(), length.capture());
        assertThat(new String(payload.getValue())).isEqualTo("Roll with it");
        assertThat(offset.getValue()).isEqualTo(0);
        assertThat(length.getValue()).isEqualTo(12);
    }

    @Test
    void onClose() {
        OnClose handler = mock(OnClose.class);
        creator.onClose(handler);
        creator.onCloseHandler.accept(1001, "Looking good");
        ArgumentCaptor<Integer> statusCode = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<String> reason = ArgumentCaptor.forClass(String.class);
        verify(handler, times(1)).accept(statusCode.capture(), reason.capture());
        assertThat(statusCode.getValue()).isEqualTo(1001);
        assertThat(reason.getValue()).isEqualTo("Looking good");
    }

    @Test
    void build() {
        IWebsocketHandler<BigDecimal> result = creator.build();
        assertThat(result).isNotNull();
    }
}