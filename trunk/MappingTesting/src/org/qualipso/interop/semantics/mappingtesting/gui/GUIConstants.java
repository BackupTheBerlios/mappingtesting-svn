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
import java.awt.Font;

/**
 * In this class all the constants used in GUI are defined. 
 * 
 * Some of the constants have been adapted from:
 * Nils Barnickel: Interoperability in eGovernment through Cross-Ontology Semantic Web Service Composition.
 * Diploma thesis, FU Berlin, August 2006.
 * (http://sourceforge.net/projects/swscomposer.)
 */
public class GUIConstants {

    public static final Color BORDER_TITLE_COLOR = new Color(100, 110, 130);
    public static final Color PANEL_BACKGROUND_COLOR = new Color(240, 240, 240);
    public static final Color INNERPANEL_BACKGROUND_COLOR = Color.WHITE;
    
    public static final Font BORDER_FONT = new Font("Arial", 0, 14);

    public static final String RED_CIRCLE_ICON = "images/red_circle.gif";
    public static final String GREEN_CIRCLE_ICON = "images/green_circle.gif";
    public static final String YELLOW_CIRCLE_ICON = "images/yellow_circle.gif";
    
    public static final int REST_MESSAGEPANEL_DIVIDER_LOCATION(int height) {
        return (int) ((height - 100) / 5.0 * 4);
    };

    public static final int ONTSANDRULES_TABBEDPANEL_DIVIDER_LOCATION(int height) {
        return (int) ((height - 100) / 5.0 * 2);
    }

    // public static final int
    // ONTOLOGIES_AND_RULES_SPLITPANE_DIVIDER_LOCATION(int width){ return (int)
    // ((width - 20) / 3.0);}
    public static final int ONTS_AND_RULES_SPLITPANE_LEFT_DIVIDER_LOCATION(int width) {
        return (int) ((width - 20) / 4.0);
    }

    public static final int ONTS_AND_RULES_SPLITPANE_RIGHT_DIVIDER_LOCATION(int width) {
        return (int) ((width - 20) / 4.0 * 2);
    }

    // 30 is taken in advance (e.g. for scrollpane slider width, etc.)
    public static final int RULES_TABLE_WIDTH(int width) {
        return (int) (((width - 20) / 4.0 * 2) - 30);
    }

    public static final int INSTANCES_SPLITPANE_DIVIDER_LOCATION(int width) {
        int testCasesDivLocation = ONTS_AND_RULES_SPLITPANE_LEFT_DIVIDER_LOCATION(width);
        return (int) (((width - 20) - testCasesDivLocation) / 2);
    }

    public static final int DEBUGGING_PANEL_DIVIDER_LOCATION(int width) {
        return (int) ((width - 20) / 2.0);
    }
    
    public static final int THREE_FOURTH_SCREEN_WIDTH(int width) {
        return (int) ((width - 20) / 4.0 * 3.0);
    }

    public static final int DIVIDERSIZE = 3;


    // GUI for source and target ontologies
    public static final String SOURCE_ONT_BORDER_TITLE = "Source Ontology";
    public static final String TARGET_ONT_BORDER_TITLE = "Target Ontology";
    public static final String BASE_NAMESPACE_LABEL = "Base Namespace: ";
    public static final String ASSERTED = "Asserted";
    public static final String INFERRED = "Inferred";
    public static final String PROPERTIES = "Properties";
    public static final String CLASS_HIERARCHY = "Class Hierarchy";
    public static final String NO_CLASSES_LABEL = "Ontology does not contain any classes.";

    // GUI for Mapping Rules
    public static final String MAPPING_RULES_BORDER_TITLE = "Mapping Rules";
    public static final String NO_RULES_CONTAINED_IN_MAPPING = "The mapping does not contain any rules";
    public static final String NAMESPACE_PREFIXES = "Namespace Prefixes:";
    public static final String EXECUTED_RULES_PANEL_TITLE = "Execution result of test case:";
    public static final String NO_RULES_SELECTED = "No rules for Debugging have been selected.";
    public static final String SELECTED_RULES_PANEL_TITLE = "Rules to be debugged:";
    public static final String DEBUGGED_RULES_PANEL_TITLE = "Debugged Rules:";

    // GUI for test cases and ontology instances
    public static final String TEST_CASES_BORDER_TITLE = "Test cases";
    public static final String INPUT_INSTANCES_BORDER_TITLE = "Input Instances";
    public static final String EXP_OUTPUT_INSTANCES_BORDER_TITLE = "Expected Output Instances";
    public static final String ACTUAL_OUTPUT_INSTANCES_BORDER_TITLE = "Mapping result";
    public static final String OUTPUT_INSTANCES_BORDER_TITLE = "Mapping result"; // is used for debugging; before was "Output Instances"

    public static final String TCCOVERAGE_AND_CONSISTENCY_BORDER_TITLE = "Test Case Coverage and Consistency Check Results";
    public static final String DESCRIPTION_BORDER_TITLE = "Test case description";
    public static final String TEST_CASE_COVERAGE = "Test case coverage:";
    public static final String CONSISTENCY_CHECK_RESULTS = "Consistency Check Results:";
    
    // Dialogs
    public static final String SELECT_TEST_PROJECT_DIRECTORY = "Select test project directory";
    public static final String SELECT_SOURCE_ONTOLOGY_FILE = "Select source ontology file";
    public static final String SELECT_TARGET_ONTOLOGY_FILE = "Select target ontology file";
    public static final String SELECT_ONTOLOGY_MAPPING_FILE = "Select ontology mapping file";
    public static final String SELECT_INPUT_INSTANCES_FILE = "Select input instances file";
    public static final String SELECT_EXPECTED_OUTPUT_INSTANCES_FILE = "Select expected output instances file";

    // Menu
    public static final String TEST_PROJECT_MENU = "Test Project";
    public static final String OPEN_TEST_PROJECT = "Open Test Project";
    public static final String NEW_TEST_PROJECT = "New Test Project";
    public static final String SAVE_TEST_PROJECT = "Save Test Project";
    public static final String CLOSE_TEST_PROJECT = "Close Test Project";

    public static final String EDIT_TEST_CASES_MENU = "Edit Test Cases";
    public static final String NEW_TEST_CASE = "New Test Case";
    public static final String DELETE_TEST_CASE = "Delete Test Case";

    public static final String RUN_TEST_CASES_MENU = "Run Test Cases";
    public static final String RUN_ALL_TEST_CASES = "Run All Test Cases";
    public static final String SHOW_TEST_REPORT = "Show Test Report";
    public static final String SAVE_TEST_REPORT = "Save Test Report";

    public static final String DEBUGGING_MENU = "Debugging";
    public static final String CLEAN_PREVIOUS_DEBUGGING_OUTPUT = "Clean Previous Debugging Output";
    public static final String SELECT_RULES_TO_BE_DEBUGGED = "Select Rules to be Debugged";
    public static final String LOAD_SOURCE_ONTOLOGY_INSTANCES = "Load Source Ontology Instances";
    public static final String START_DEBUGGING = "Start Debugging";

    public static final String OK = "OK";
    public static final String CANCEL = "Cancel";
    public static final String BROWSE = "Browse...";
}
