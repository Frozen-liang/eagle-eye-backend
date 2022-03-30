package com.sms.eagle.eye.backend.utils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import java.io.IOException;
import org.junit.jupiter.api.Test;

public class KeepAsJsonDeserializerTest {

    KeepAsJsonDeserializer deserializer = spy(KeepAsJsonDeserializer.class);

    @Test
    void deserialize_test() throws IOException {
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);
        // mock jp.getCodec()
        ObjectCodec objectCodec = mock(ObjectCodec.class);
        when(jp.getCodec()).thenReturn(objectCodec);
        // mock objectCodec.readTree()
        TreeNode treeNode = mock(TreeNode.class);
        when(objectCodec.readTree(jp)).thenReturn(treeNode);
        // mock treeNode.toString()
        String value = "value";
        when(treeNode.toString()).thenReturn(value);
        // invoke
        String result =  deserializer.deserialize(jp, context);
        // assert
        assertThat(result).isEqualTo(value);
    }
}