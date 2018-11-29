package com.turing.turingsdksample.bean;

public class RstDataBean {

	private String version;
	private String res;
	private String rec;
	private double conf;
	private TimeBean time;

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getRes() {
		return res;
	}

	public void setRes(String res) {
		this.res = res;
	}

	public String getRec() {
		return rec;
	}

	public void setRec(String rec) {
		this.rec = rec;
	}

	public double getConf() {
		return conf;
	}

	public void setConf(double conf) {
		this.conf = conf;
	}

	public TimeBean getTime() {
		return time;
	}

	public void setTime(TimeBean time) {
		this.time = time;
	}

	public static class TimeBean {

		private int wav;
		private int vad;
		private int sys;
		private int ses;
		private int dly;
		private int dfm;

		public int getWav() {
			return wav;
		}

		public void setWav(int wav) {
			this.wav = wav;
		}

		public int getVad() {
			return vad;
		}

		public void setVad(int vad) {
			this.vad = vad;
		}

		public int getSys() {
			return sys;
		}

		public void setSys(int sys) {
			this.sys = sys;
		}

		public int getSes() {
			return ses;
		}

		public void setSes(int ses) {
			this.ses = ses;
		}

		public int getDly() {
			return dly;
		}

		public void setDly(int dly) {
			this.dly = dly;
		}

		public int getDfm() {
			return dfm;
		}

		public void setDfm(int dfm) {
			this.dfm = dfm;
		}
	}
}
