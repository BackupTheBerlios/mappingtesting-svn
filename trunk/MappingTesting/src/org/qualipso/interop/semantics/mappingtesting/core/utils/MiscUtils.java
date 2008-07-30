/*
 * Copyright (c) 1995 - 2008 Sun Microsystems, Inc.  All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Sun Microsystems nor the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */ 

package org.qualipso.interop.semantics.mappingtesting.core.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.Vector;


/**
 * Miscellaneous Utils needed by the tool
 *
 */
public class MiscUtils {
    
    /**
     * Copies a file, located at fromFilePath into toFileDir. 
     * File name remains the same.
     * @param fromFilePath
     * @param toFileDir
     */
    public static String copyFile(String fromFilePath, String toFileDir){
        FileReader inputStream = null;
        FileWriter outputStream = null;
        
        String copiedFileName = (new File(fromFilePath)).getName();
        String toFilePath = toFileDir + "/" + copiedFileName;
        
        try {
            inputStream = new FileReader(fromFilePath);
            outputStream = new FileWriter(toFilePath);

            int c;
            while ((c = inputStream.read()) != -1) {
                outputStream.write(c);
            }
            return toFilePath;
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Determines current directory URI, where the program has been started.
     */
    public static String determineProgramBasePathURI() {
        File currentDir = new File(".");
        String currentDirURI = currentDir.toURI().toString();
        String currentDirString = currentDirURI.substring(0, currentDirURI.length() - 3);
        return currentDirString;
    }

    /**
     * Determines URI of filePath.
     * 
     * @param filePath
     * @return
     */
    public static String determineFilePathURI(String filePath) {
        File file = new File(filePath);
        String fileURI = file.toURI().toString();
        return fileURI;
    }

    /**
     * Replaces chars, defined by their indexes (charIndexes), in the
     * initialText by the replacement. The implementation was been partially adopted from:
     * http://www.javapractices.com/topic/TopicAction.do?Id=80
     * 
     * @param initialText
     * @param charIndexes
     * @param replacement
     * @return
     */
    public static String replace(String initialText, Vector charIndexes, String replacement) {
        StringBuffer result = new StringBuffer();
        int startIdx = 0;
        for (Iterator iter = charIndexes.iterator(); iter.hasNext();) {
            int index = (Integer) iter.next();
            // grab a part of aInput which does not include aOldPattern
            result.append(initialText.substring(startIdx, index));
            // add aNewPattern to take place of aOldPattern
            result.append(replacement);

            // reset the startIdx to just after the current match, to see
            // if there are any further matches
            startIdx = index + 1;
        }
        // the final chunk will go to the end of aInput
        result.append(initialText.substring(startIdx));
        return result.toString();
    }

    /**
     * Deletes a non-empty directory, defined by dirPath from the file system.
     * Returns true, if directory and all the files, contained in it, have been
     * deleted.
     * 
     * @param dirPath
     * @return
     */
    public static boolean deleteNonEmptyDirectory(String dirPath) {
        File dir = new File(dirPath);
        if (dir.exists() && dir.isDirectory()) {
            File[] files = dir.listFiles();
            for (int i = 0; i < files.length; i++) {
                File file = files[i];
                boolean fileDeleted = file.delete();
                if (!fileDeleted)
                    return false;
            }
            return dir.delete();
        } else
            return false;
    }

    public static String replaceSpaces(String toReplace) {
        if (toReplace.indexOf(" ") == -1) return toReplace;
        
        // this char has to be replaced by: %20
        char SPACE = ' ';

        Vector spaceIndexes = new Vector();

        char[] charArray = toReplace.toCharArray();
        for (int i = 0; i < charArray.length; i++) {
            char c = charArray[i];
            if (c == SPACE)
                spaceIndexes.add(i);
        }
        String replacedText = MiscUtils.replace(toReplace, spaceIndexes, "%20");
        return replacedText;
    }

}
