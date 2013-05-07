package org.romaframework.aspect.hook;

import org.romaframework.aspect.hook.annotation.HookScope;
import org.romaframework.aspect.hook.feature.HookActionFeatures;
import org.romaframework.aspect.hook.feature.HookFieldFeatures;
import org.romaframework.core.Roma;
import org.romaframework.core.aspect.AspectConfigurator;
import org.romaframework.core.schema.SchemaAction;
import org.romaframework.core.schema.SchemaClassDefinition;
import org.romaframework.core.schema.SchemaEvent;
import org.romaframework.core.schema.SchemaField;

public class HookAspectConfigurator implements AspectConfigurator {

	public void configField(SchemaField iField) {

		HookScope scope = iField.getFeature(HookFieldFeatures.SCOPE);
		if (scope != null)
			scope = HookScope.SESSION;
		Roma.aspect(HookAspect.class).registerHook(iField, HookFieldFeatures.FIELD, scope);

	}

	public void configAction(SchemaAction iAction) {

		HookScope scope = iAction.getFeature(HookActionFeatures.SCOPE) != null ? iAction.getFeature(HookActionFeatures.SCOPE) : HookScope.SESSION;

		Roma.aspect(HookAspect.class).registerHook(iAction, HookActionFeatures.HOOK_AROUND_ACTION, scope);
		Roma.aspect(HookAspect.class).registerHook(iAction, HookActionFeatures.HOOK_BEFORE_ACTION, scope);
		Roma.aspect(HookAspect.class).registerHook(iAction, HookActionFeatures.HOOK_AFTER_ACTION, scope);

		Roma.aspect(HookAspect.class).registerHook(iAction, HookActionFeatures.HOOK_AROUND_FIELD_READ, scope);
		Roma.aspect(HookAspect.class).registerHook(iAction, HookActionFeatures.HOOK_BEFORE_FIELD_READ, scope);
		Roma.aspect(HookAspect.class).registerHook(iAction, HookActionFeatures.HOOK_AFTER_FIELD_READ, scope);

		Roma.aspect(HookAspect.class).registerHook(iAction, HookActionFeatures.HOOK_AROUND_FIELD_READ, scope);
		Roma.aspect(HookAspect.class).registerHook(iAction, HookActionFeatures.HOOK_BEFORE_FIELD_READ, scope);
		Roma.aspect(HookAspect.class).registerHook(iAction, HookActionFeatures.HOOK_AFTER_FIELD_READ, scope);

		Roma.aspect(HookAspect.class).registerHook(iAction, HookActionFeatures.HOOK_AROUND_EVENT, scope);
		Roma.aspect(HookAspect.class).registerHook(iAction, HookActionFeatures.HOOK_AROUND_EVENT, scope);
		Roma.aspect(HookAspect.class).registerHook(iAction, HookActionFeatures.HOOK_AFTER_EVENT, scope);
	}

	public void beginConfigClass(SchemaClassDefinition iClass) {

	}

	public void configClass(SchemaClassDefinition iClass) {

	}

	public void configEvent(SchemaEvent iEvent) {

	}

	public void endConfigClass(SchemaClassDefinition iClass) {

	}

}
