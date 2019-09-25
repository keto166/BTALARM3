#include "ColorTool.h"
#include "Arduino.h"


ColorTool::ColorTool() {

}

ColorTool::~ColorTool() {

}

void ColorTool::setup0() {
	clear_var();
	m_pins = 0;
	m_mode = 0;
	m_state_count = 1;
	m_state = 0;
	m_prev_time = millis();
	m_time_state[0] = 1000;
}

void ColorTool::setup1(int pinout1) {

	clear_var();
	m_pinout[0] = pinout1;
	m_pins = 1;
	m_mode = 1;

	m_state_count = 4;
	m_state = 0;
	m_prev_time = millis();

	m_time_state[0] = 4000;
	m_time_state[1] = 10000;
	m_time_state[2] = 4000;
	m_time_state[3] = 100;

	state_change_action();
}

void ColorTool::setup2(int pinout1, int pinout2) {

	clear_var();
	m_pinout[0] = pinout1;
	m_pinout[1] = pinout2;
	m_pins = 2;
	m_mode = 2;
	m_state_count = 5;
	m_state = 0;
	m_prev_time = millis();

	m_time_state[0] = 6000;
	m_time_state[1] = 10000;
	m_time_state[2] = 10000;
	m_time_state[3] = 10000;
	m_time_state[4] = 2000;

	state_change_action();
}

void ColorTool::setup3(int pinout1, int pinout2, int pinout3) {

	clear_var();
	m_pinout[0] = pinout1;
	m_pinout[1] = pinout2;
	m_pinout[2] = pinout3;
	m_pins = 3;
	m_mode = 3;
	m_state_count = 6;
	m_state = 0;
	m_prev_time = millis();

	for (int i = 0; i < 6; i++) {
		m_time_state[i] = 4000;
	}

	state_change_action();
}

void ColorTool::process() {

	m_current_time = millis();
	m_time += (m_current_time - m_prev_time);
	if (m_time > m_time_state[m_state]) {
		m_time -= m_time_state[m_state];
		m_state++;
		if (m_state == m_state_count) {
			m_state = 0;
		}
		state_change_action();
	}
	state_action();
	m_prev_time = m_current_time;

}

void ColorTool::state_change_action() {

	switch(m_mode) {
		case 0:		//off

		break;
		case 1:		//1 color
			switch(m_state) {
				case 0:
					m_colorval[0] = 0;
				break;
				case 1:

				break;
				case 2:

				break;
				case 3:

				break;
			}

		break;

		case 2:		//2 color
			switch(m_state) {
				case 0:
					m_colorval[0] = 0;
					m_colorval[1] = 0;
				break;
				case 1:
					m_colorval[0] = 255;
					m_colorval[1] = 0;
				break;
				case 2:
					m_colorval[0] = 255;
					m_colorval[1] = 255;
				break;
				case 3:
					m_colorval[0] = 0;
					m_colorval[1] = 255;
				break;
				case 4:
					m_colorval[0] = 0;
					m_colorval[1] = 0;
				break;

			}
		break;

		case 3:		//3 color
			switch(m_state) {
				case 0:
					m_colorval[0] = 0;
					m_colorval[1] = 0;
					m_colorval[2] = 255;
				break;
				case 1:

				break;
				case 2:

				break;
				case 3:

				break;
				case 4:

				break;
				case 5:

				break;
			}
		break;

	}
	write();
}

void ColorTool::state_action() {

	switch(m_mode) {
		case 0:		//Off

		break;
		case 1:		//1 color
			switch(m_state) {
				case 0:
					m_colorval[0] = val_calc(true);
				break;
				case 1:
					//wait
				break;
				case 2:
					m_colorval[0] = val_calc(false);
				break;
				case 3:
					//wait
				break;
			}

		break;

		case 2:		//2 color
			switch(m_state) {
				case 0:
					m_colorval[0] = val_calc(true);
				break;
				case 1:
					m_colorval[1] = val_calc(true);
				break;
				case 2:
					m_colorval[0] = val_calc(false);
				break;
				case 3:
					m_colorval[1] = val_calc(false);
				break;
				case 4:
					//wait
				break;

			}
		break;

		case 3:		//3 color
			switch(m_state) {
				case 0:
					m_colorval[0] = val_calc(true);
				break;
				case 1:
					m_colorval[2] = val_calc(false);
				break;
				case 2:
					m_colorval[1] = val_calc(true);
				break;
				case 3:
					m_colorval[0] = val_calc(false);
				break;
				case 4:
					m_colorval[2] = val_calc(true);
				break;
				case 5:
					m_colorval[1] = val_calc(false);
				break;
			}
		break;

	}
	write();
}

void ColorTool::write() {

	for (int i = 0; i < m_pins; i++) {
		if (m_colorval[i] > 255) {
			m_colorval[i] = 255;
			Serial.print("Color " + (String) i + "set to 255");
			Serial.println();
		} else if (m_colorval[i] < 0) {
			m_colorval[i] = 0;
			Serial.print("Color " + (String) i + "set to 0");
			Serial.println();
    	}
		analogWrite(m_pinout[i],m_colorval[i]);
	}
}

void ColorTool::clear_var() {
		m_mode=0;
		for (int i = 0; i < 3; i++) {
			m_pinout[i] = 0;
		}
		m_pins=0;
		for (int i = 0; i < 3; i++) {
			m_colorval[i] = 0;
		}
		m_state_count=0;
		for (int i = 0; i < 6; i++) {
			m_time_state[i] = 0;
		}
		m_state=0;
		m_current_time=millis();
		m_prev_time=m_current_time;
		m_time = 0;

}

int ColorTool::val_calc(bool rise) {
	double temp;
	double t2;
	t2 = (double) m_time / m_time_state[m_state];
	if (rise) {
		temp = pow(t2, 4) * 255;
	} else {
		temp = (pow(1-t2, 4)) * 255;
	}
	/* Debugging
	Serial.print("Temp = " + (String) temp + ", t2 = " + (String) t2);
	Serial.print("state = " + m_state);
	Serial.println();
	*/
	return (int) temp;

}

