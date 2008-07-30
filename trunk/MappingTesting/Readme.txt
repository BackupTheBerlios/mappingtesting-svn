Installation and Usage of the Mapping Testing Tool under Windows XP:
=============================================================================

1. Install Java Virtual Machine

2. Install ANT
- Unzip apache-ant-1.7.0-bin.zip
- Set ANT_HOME environment variable to the directory, where Ant has been installed.
- Add $ANT_HOME\bin directory to the PATH environment variable.

3. Install Protege build 504
- Run installation\install_protege.exe

4. Install Jess Rule Engine version 7.0p2
- Download Jess version 7.0p2 from http://herzberg.ca.sandia.gov/ and unzip it. Note: the mapping testing tool does not work with Jess 7.1.
- Copy the file jess.jar into lib/edu.stanford.smi.protegex.owl and replace the existing one.

5. Configure Mapping Testing Tool
- open the file build.xml
- Set a JVM argument "-Dprotege.dir". For this edit a target "startMappingTestingTool"
  by adding the following XML element to the "java" element:
  
  <jvmarg value="-Dprotege.dir=PROTEGE_HOME"/>	, whereby PROTEGE_HOME is the Protege installation directory.

6. Start Mapping Testing Tool
- Run startMappingTestingtTool.bat
- An exemplary test project is provided in ScenarioTestProject/ScenarioTestProject.owl
   
7. Recompile Mapping Testing Tool (if needed)
- Run compile.bat

