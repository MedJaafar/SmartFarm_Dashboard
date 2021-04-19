package com.smart.farm.models;

public class WateringData {

		private 	double volume;
		private 	double time;
		private		String date;
		private 	double moisitureAvg;
			/**
			 * @param volume
			 * @param time
			 * @param date
			 */
			public WateringData(double volume, double time, String date,double moisitureAvg) {
				super();
				this.volume = volume;
				this.time = time;
				this.date = date;
				this.moisitureAvg = moisitureAvg;
				
			}
			public double getVolume() {
				return volume;
			}
			public void setVolume(double volume) {
				this.volume = volume;
			}
			public double getTime() {
				return time;
			}
			public void setTime(double time) {
				this.time = time;
			}
			public String getDate() {
				return date;
			}
			public void setDate(String date) {
				this.date = date;
			}
			public double getMoisitureAvg() {
				return moisitureAvg;
			}
			public void setMoisitureAvg(double moisitureAvg) {
				this.moisitureAvg = moisitureAvg;
			}	
		}

