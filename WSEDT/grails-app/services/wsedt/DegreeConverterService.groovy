package wsedt

import javax.jws.WebParam

class DegreeConverterService {
	static expose=['xfire']
	
	
	float celsiusToFahrenheit(@WebParam(name="tc", header=true) float tc) {
		float tf = (9f / 5f) * tc + 32f
	}
	
	float fahrenheitToCelsius(@WebParam(name="tf", header=true) float tf) {
		 float tc = (5f / 9f) * (tf - 32f)
	}
}