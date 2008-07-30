/*
 * Snoggle uses the BSD License from the Open Source Initiative,  
 * http://www.opensource.org/licenses/bsd-license.php
 * 
 * Copyright (c) 2007, BBN Technologies and Northrop Grumman Corporation
 * 
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions 
 * are met:
 * 
 *     * Redistributions of source code must retain the above copyright 
 *     notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above 
 *     copyright notice, this list of conditions and the following disclaimer 
 *     in the documentation and/or other materials provided with the 
 *     distribution.
 *     * Neither the name of BBN Technologies, Northrop Grumman Corporation, 
 *     nor the names of its contributors may be used to endorse or promote 
 *     products derived from this software without specific prior written 
 *     permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS 
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT 
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR 
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT 
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, 
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED 
 * TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR 
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF 
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING 
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS 
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * 
 */
package org.qualipso.interop.semantics.mappingtesting.gui.testcases.execution;

import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

/**
 * This JFrame displays the text of test report.
 * 
 */
public class TestReportTextPreview extends JFrame {

    protected String text;

    public TestReportTextPreview(String title, String text) {
        super(title);
        this.text = text;
        initialize();
        setVisible(true);
    }

    protected void initialize() {
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(createPreview(), BorderLayout.CENTER);
        // getContentPane().add(createButtons(), BorderLayout.SOUTH);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        pack();
    }

    protected JPanel createPreview() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        JTextPane tp = new JTextPane();
        tp.setFont(new Font("Courier", Font.PLAIN, 12));
        tp.setText(text);
        tp.setEditable(false);
        panel.add(new JScrollPane(tp), BorderLayout.CENTER);
        return panel;
    }

}
