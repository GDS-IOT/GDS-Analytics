package com.gds.analytics.converter;

public interface Converter<T> {

    public T convert(byte []data);
}
