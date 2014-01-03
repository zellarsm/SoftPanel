package utils;

import javax.swing.text.NavigationFilter;
import javax.swing.text.Position;
import javax.swing.text.NavigationFilter.FilterBypass;

public class MyNavigationFilter extends NavigationFilter {
	 @Override
     public void setDot(FilterBypass fb, int dot, Position.Bias bias) {
         if (dot >= 3) {
             fb.setDot(0, bias);
            //field1.transferFocus();
             return;
         }
         fb.setDot(dot, bias);
     }

     @Override
     public void moveDot(FilterBypass fb, int dot, Position.Bias bias) {
         if (dot >= 3) { 
             fb.setDot(0, bias);
             //field2.transferFocus();
             return;
         }
         fb.moveDot(dot, bias);
     }

 };

