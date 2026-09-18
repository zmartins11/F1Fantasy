package com.example.demo.f1.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ConstructorResponse {

		@JsonProperty("MRData")
		private MrData mrData;

		@JsonProperty("Constructors")
        Constructor constructor;

		public MrData getMrData() {
			return mrData;
		}

		public void setMrData(MrData mrData) {
			this.mrData = mrData;
		}

		public Constructor getConstructor() {
			return constructor;
		}

		public void setConstructor(Constructor constructor) {
			this.constructor = constructor;
		}
		
		

	
		
}
