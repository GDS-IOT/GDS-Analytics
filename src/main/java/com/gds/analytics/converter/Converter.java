package com.gds.analytics.converter;

import com.gds.domain.GDSData;

public interface Converter<T> {

    public T convert(GDSData data);
}
