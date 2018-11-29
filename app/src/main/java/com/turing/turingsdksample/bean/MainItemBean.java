package com.turing.turingsdksample.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by xiongcen on 17/5/1.
 */

public class MainItemBean implements Serializable {

	private String id;
	private String name;
	private String icon;
	private String textColor;
	private String textSize;
	private String xiaoxueView;
	private String gridViewleft;
	private String gridViewright;
	private List<MainContentBean> content;
	
	
	public String getTextSize() {
		return textSize;
	}

	public void setTextSize(String textSize) {
		this.textSize = textSize;
	}


	private String qingli;
	private String xiaoxue;
		
	
	
	 public String getXiaoxueView() {
		return xiaoxueView;
	}

	public void setXiaoxueView(String xiaoxueView) {
		this.xiaoxueView = xiaoxueView;
	}

	public String getGridViewleft() {
		return gridViewleft;
	}

	public void setGridViewleft(String gridViewleft) {
		this.gridViewleft = gridViewleft;
	}

	public String getGridViewright() {
		return gridViewright;
	}

	public void setGridViewright(String gridViewright) {
		this.gridViewright = gridViewright;
	}

	public String getTextColor() {
		return textColor;
	}

	public void setTextColor(String textColor) {
		this.textColor = textColor;
	}


	public String getQingli() {
		return qingli;
	}

	public void setQingli(String qingli) {
		this.qingli = qingli;
	}

	public String getXiaoxue() {
		return xiaoxue;
	}

	public void setXiaoxue(String xiaoxue) {
		this.xiaoxue = xiaoxue;
	}



	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public List<MainContentBean> getContent() {
		return content;
	}

	public void setContent(List<MainContentBean> content) {
		this.content = content;
	}


	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}


	public static class MainContentBean implements Serializable {
		/**
		 * id : scjd
		 * name : 诗词经典
		 * url : cat.png
		 */
		private String id;
		private String name;
		private String url;
		private String imagerUrl;
		private String type;
		private String value;
		public String getValue() {
			return value;
		}
		public void setValue(String value) {
			this.value = value;
		}
		private String pkgname;
		private String clsname;
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getUrl() {
			return url;
		}
		public void setUrl(String url) {
			this.url = url;
		}
		public String getImagerUrl() {
			return imagerUrl;
		}
		public void setImagerUrl(String imagerUrl) {
			this.imagerUrl = imagerUrl;
		}
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}
		public String getPkgname() {
			return pkgname;
		}
		public void setPkgname(String pkgname) {
			this.pkgname = pkgname;
		}
		public String getClsname() {
			return clsname;
		}
		public void setClsname(String clsname) {
			this.clsname = clsname;
		}
	}
}
