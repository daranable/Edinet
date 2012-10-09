package net.maltera.daranable.edinet;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 * Hello world!
 *
 */
public class Edinet 
extends javax.swing.JFrame
{
	public Edinet( String name )
	{
		super( name );
	}
	
    public static void main( String[] args )
    {
        Edinet frame = new Edinet( "Hello, World!" );
        
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        
        JLabel emptyLabel = new JLabel();
        
        frame.getContentPane().add(emptyLabel, BorderLayout.CENTER);
        
        frame.pack();
        
        frame.setVisible( true );
    }
}
