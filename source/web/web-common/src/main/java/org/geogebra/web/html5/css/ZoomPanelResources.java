package org.geogebra.web.html5.css;

import org.geogebra.web.resources.SVGResource;
import org.gwtproject.resources.client.ClientBundle;
import org.gwtproject.resources.client.Resource;

/**
 * Icons for zoom panel.
 */
@Resource
public interface ZoomPanelResources extends ClientBundle {

	ZoomPanelResources INSTANCE = new ZoomPanelResourcesImpl();

	@Source("org/geogebra/common/icons/svg/web/matDesignIcons/ev/ic_fullscreen_black_18px.svg")
	SVGResource fullscreen_black18();

	@Source("org/geogebra/common/icons/svg/web/matDesignIcons/ev/ic_fullscreen_exit_black_18px.svg")
	SVGResource fullscreen_exit_black18();

	@Source("org/geogebra/common/icons/svg/web/matDesignIcons/contextMenu/ic_home_black_24px.svg")
	SVGResource home_zoom_black18();

	@Source("org/geogebra/common/icons/svg/web/matDesignIcons/ev/target.svg")
	SVGResource target();
}
