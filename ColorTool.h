#ifndef COLORTOOL_H
#define COLORTOOL_H

#include "Arduino.h"
//#include <Math>
//#include <String>


/*
	This is the basic color pattern programming that will be
	used in the Arduino setup for Bluetooth light control.

	Light mode 0: off (default)

	States for 1 color, light mode 1:
	0 - Color 1 rising
	1 - Color 1 lit
	2 - Color 1 falling
	3 - Color 1 dark

	States for 2 colors, light mode 2:
	0 - Color 1 rising		Color 2 dark
	1 - Color 1 lit			Color 2 rising
	2 - Color 1 falling		Color 2 lit
	3 - Color 1 dark		Color 2 falling
	4 - Color 1 dark		Color 1 dark

	States for 3 colors, light mode 3:
	0 - Color 1 rising		Color 2 dark		Color 3 lit
	1 - Color 1 lit			Color 2 dark		Color 3 falling
	2 - Color 1 lit			Color 2 rising		Color 3 dark
	3 - Color 1 falling		Color 2 lit			Color 3 dark
	4 - Color 1 dark		Color 2 lit			Color 3 rising
	5 - Color 1 dark		Color 2 falling		Color 3 lit



	This assumes equal time for each state.

	The write function, which will be used to send commands to the
	PWM slots, is planned to be overwritten in derived classes for
	multiple light strands/ColorTool objects.

	Error management still needs to be implemented, such as lag detection,
	invalid setup variables, etc.


	Thomas Ryan
	ryanto166@gmail.com
	Nov 13, 2017
*/

class ColorTool
{
	public:

		ColorTool();
		~ColorTool();


		void process();
		void setup0();
		void setup1(int pinout1);
		void setup2(int pinout1, int pinout2);
		void setup3(int pinout1, int pinout2, int pinout3);

	protected:

		int m_mode;						//light mode, 0, 1, 2 or 3
		int m_pinout[3];				//pin outs for Color 1, 2, and 3
		int m_pins;						//number of pins used
		int m_colorval[3];				//values for Color 1, 2, and 3
		int m_state_count;				//number of states used
		unsigned long  m_time_state[6];	//times for the states
		int m_state;			 		//current state
		unsigned long m_current_time;	//current time
		unsigned long m_prev_time;		//previous time
		int m_time;						//time progressed in the current state in the cycle
		void state_change_action();		//actions that run each time the state changes
		void state_action();			//actions that run between state changes
		void write();					//function to output color values to board
		void clear_var();				//function used to reset the object variables
		int val_calc(bool rise);		//function to calculate the colorval based on current time and direction



	private:

};

#endif // COLORTOOL_H
