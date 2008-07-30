/*
 * IST-FP6-034763 QualiPSo: QualiPSo is a unique alliance
 * of European, Brazilian and Chinese ICT industry players,
 * SMEs, governments and academics to help industries and
 * governments fuel innovation and competitiveness with Open
 * Source software. To meet that goal, the QualiPSo consortium
 * intends to define and implement the technologies, processes
 * and policies to facilitate the development and use of Open
 * Source software components, with the same level of trust
 * traditionally offered by proprietary software. QualiPSo is
 * partially funded by the European Commission under EU’s sixth
 * framework program (FP6), as part of the Information Society
 * Technologies (IST) initiative. 
 *
 * This program has been created as part of the QualiPSo work
 * package on "Semantic Interoperability". The basic idea of this work    
 * package is to demonstrate how semantic technologies can be used to
 * cope with the diversity and heterogeneity of software and services
 * in the OSS domain.
 *
 * You can redistribute this program and/or modify it under the terms
 * of the GNU Lesser General Public License version 3 (LGPL v3.0).
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  
 *
 * You should have received a copy of the LGPL
 * along with this program; if not, please have a look at
 * http://www.gnu.org/licenses/lgpl.html
 * to obtain the full license text.
 *
 * Author of this program: 
 * Fraunhofer Institute FOKUS, http://www.fokus.fraunhofer.de
 *
 *
 */

package org.qualipso.interop.semantics.mappingtesting.gui;

import java.awt.Color;

import javax.swing.border.TitledBorder;

/**
 * MessagePanel realized as singleton to access from everywhere.
 * 
 */
import java.awt.*;
import javax.swing.*;
import java.lang.reflect.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.Document;
import javax.swing.text.*;

/**
 * Message panel GUI component. Singleton object which prints messages inside
 * the Mapping Testing Tool.
 * 
 * Source: 
 * Nils Barnickel: Interoperability in eGovernment through Cross-Ontology Semantic Web Service Composition.
 * Diploma thesis, FU Berlin, August 2006.
 * (http://sourceforge.net/projects/swscomposer.)
 */
public class MessagePanel extends JScrollPane {

    // singleton instance
    private static MessagePanel instance = new MessagePanel();;

    // color settings for text messages
    private static final SimpleAttributeSet redText = new SimpleAttributeSet();
    static {
        StyleConstants.setForeground(redText, new Color(0.7f, 0.2f, 0.2f));// Color.red);
    }

    private static final SimpleAttributeSet blackText = new SimpleAttributeSet();
    static {
        StyleConstants.setForeground(blackText, Color.black);
    }

    private static final SimpleAttributeSet blueText = new SimpleAttributeSet();
    static {
        StyleConstants.setForeground(blueText, GUIConstants.BORDER_TITLE_COLOR);
    }

    private static final SimpleAttributeSet greenText = new SimpleAttributeSet();
    static {
        StyleConstants.setForeground(greenText, new Color(0.5f, 0.7f, 0.55f));
    }

    // enable same output to console
    private static boolean console = false;

    private JTextPane jTextPane = new JTextPane();

    private MessagePanel() {

        try {
            this.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Message Panel",
                    TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION,
                    GUIConstants.BORDER_FONT, GUIConstants.BORDER_TITLE_COLOR));
            this.setBackground(GUIConstants.PANEL_BACKGROUND_COLOR);
            jTextPane.setEditable(false);
            this.getViewport().add(jTextPane);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static MessagePanel getInstance() {
        return instance;
    }

    private static void _appendError(final String error) {
        try {
            final Document doc = instance.jTextPane.getDocument();
            doc.insertString(doc.getLength(), error, redText);
            if (console)
                System.out.print(error);
        } catch (BadLocationException ex) {
            ex.printStackTrace();
        }
    }

    public static void appendError(final String error) {
        if ("AWT-EventQueue-0".equals(Thread.currentThread().getName())) {
            _appendError(error);
        } else {
            try {
                EventQueue.invokeAndWait(new Runnable() {
                    public void run() {
                        _appendError(error);
                    }
                });
            } catch (InvocationTargetException ex) {
                ex.printStackTrace();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void _appendOut(final String out) {
        try {
            final Document doc = instance.jTextPane.getDocument();
            doc.insertString(doc.getLength(), out, blackText);
            if (console)
                System.out.print(out);
        } catch (BadLocationException ex) {
            ex.printStackTrace();
        }
    }

    public static void appendOut(final String out) {
        if ("AWT-EventQueue-0".equals(Thread.currentThread().getName())) {
            _appendOut(out);
        } else {
            try {
                EventQueue.invokeAndWait(new Runnable() {
                    public void run() {
                        _appendOut(out);
                    }
                });
            } catch (InvocationTargetException ex) {
                ex.printStackTrace();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void _appendOutBlue(final String out) {
        try {
            final Document doc = instance.jTextPane.getDocument();
            doc.insertString(doc.getLength(), out, blueText);
            if (console)
                System.out.print(out);
        } catch (BadLocationException ex) {
            ex.printStackTrace();
        }
    }

    public static void appendOutBlue(final String out) {
        if ("AWT-EventQueue-0".equals(Thread.currentThread().getName())) {
            _appendOutBlue(out);
        } else {
            try {
                EventQueue.invokeAndWait(new Runnable() {
                    public void run() {
                        _appendOutBlue(out);
                    }
                });
            } catch (InvocationTargetException ex) {
                ex.printStackTrace();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void appendOutBlueln(final String out) {
        appendOutBlue(out + "\n");
    }

    private static void _appendSuccess(final String suc) {
        try {
            final Document doc = instance.jTextPane.getDocument();
            // doc.insertString(doc.getLength(), " ... ", blackText);
            doc.insertString(doc.getLength(), suc, greenText);
            if (console)
                System.out.print(suc);
        } catch (BadLocationException ex) {
            ex.printStackTrace();
        }
    }

    public static void appendSuccess(final String suc) {
        if ("AWT-EventQueue-0".equals(Thread.currentThread().getName())) {
            _appendSuccess(suc);
        } else {
            try {
                EventQueue.invokeAndWait(new Runnable() {
                    public void run() {
                        _appendSuccess(suc);
                    }
                });
            } catch (InvocationTargetException ex) {
                ex.printStackTrace();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void appendOutln(final String out) {
        appendOut(out + "\n");
    }

    public static void appendErrorln(final String out) {
        appendError(out + "\n");
    }

    public static void appendSuccessln(final String suc) {
        appendSuccess(suc + "\n");
    }
}
