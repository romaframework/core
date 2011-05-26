package org.romaframework.core.test.features;
import org.romaframework.aspect.core.annotation.AnnotationConstants;
import org.romaframework.aspect.core.annotation.CoreClass;
import org.romaframework.aspect.core.annotation.CoreField;
import org.romaframework.aspect.validation.annotation.ValidationField;

@CoreClass(orderActions = "aaaa")
public class DomainClassFeatures {

	@CoreField(embedded = AnnotationConstants.TRUE)
	private String	campo;

	@ValidationField(required = AnnotationConstants.TRUE, match = "aaaa")
	public String getCampo() {
		return campo;
	}

	public void setCampo(String campo) {
		this.campo = campo;
	}

}
