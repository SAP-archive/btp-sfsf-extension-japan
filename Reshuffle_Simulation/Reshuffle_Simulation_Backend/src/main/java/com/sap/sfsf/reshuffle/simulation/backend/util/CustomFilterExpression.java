package com.sap.sfsf.reshuffle.simulation.backend.util;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.olingo.odata2.api.edm.EdmEntityType;

import com.sap.cloud.sdk.odatav2.connectivity.FilterExpression;
import com.sap.cloud.sdk.odatav2.connectivity.ODataType;

public class CustomFilterExpression extends FilterExpression {

	@Nonnull
	private final String field;
	@Nonnull
	private final String operator;
	@Nonnull
	private final String value;

	public CustomFilterExpression(String field, String operator, String value) {
		super(field, operator, ODataType.of(value));
		this.field = field;
		this.operator = operator;
		this.value = value;
	}

	@Override
	@Nonnull
	public String toString() {
		return String.format("%s %s %s", field, operator, value);
	}

	@Override
	@Nonnull
	public String toString(@Nullable final EdmEntityType entityType) {
		return toString();
	}

}
