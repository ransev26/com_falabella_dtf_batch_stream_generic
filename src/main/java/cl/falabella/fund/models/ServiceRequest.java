package cl.falabella.fund.models;

import cl.falabella.fund.exceptions.RequestException;

public class ServiceRequest {

	private String inputSubscription;
	private String project;
	private String stagingLocation;
	private String tempLocation;
	private String runner;
	private String gcsPath;
	private String outputTopic;
	private String dataFlowName;



	public ServiceRequest(String inputSubscription, String project,
						  String stagingLocation, String tempLocation, String runner,
						  String gcsPath, String outputTopic, String dataFlowName) {
		this.inputSubscription = inputSubscription;
		this.project = project;
		this.stagingLocation = stagingLocation;
		this.tempLocation = tempLocation;
		this.runner = runner;
		this.dataFlowName = dataFlowName;
		this.gcsPath = gcsPath;
		this.outputTopic = outputTopic;

	}

	public String getInputSubscription() {
		return inputSubscription;
	}

	public String getProject() {
		return project;
	}

	public String getStagingLocation() {
		return stagingLocation;
	}

	public String getTempLocation() {
		return tempLocation;
	}

	public String getRunner() {
		return runner;
	}

	public String getGcsPath() { return gcsPath; }

	public String getOutputTopic() { return outputTopic; }

	public String getDataFlowName() { return dataFlowName;}

	private void validateInputSubscription() throws RequestException {
		if (this.getInputSubscription() == null || this.getInputSubscription().isEmpty())
			throw new RequestException("inputSubscription cannot be null or empty!");
	}

	private void validateProject() throws RequestException {
		if (this.getProject() == null || this.getProject().isEmpty())
			throw new RequestException("project cannot be null or empty!");
	}

	private void validateStagingLocation() throws RequestException {
		if (this.getStagingLocation() == null || this.getStagingLocation().isEmpty())
			throw new RequestException("stagingLocation cannot be null or empty!");
	}

	private void validateTempLocation() throws RequestException {
		if (this.getTempLocation() == null || this.getTempLocation().isEmpty())
			throw new RequestException("tempLocation cannot be null or empty!");
	}

	private void validateRunner() throws RequestException {
		if (this.getRunner() == null || this.getRunner().isEmpty())
			throw new RequestException("runner cannot be null or empty!");
	}


	private void validategetGcsPath() throws RequestException {
		if (this.getGcsPath() == null || this.getGcsPath().isEmpty())
			throw new RequestException("gcsPath cannot be null or empty!");
	}

	private void validateOutputTopic() throws RequestException {
		if (this.getOutputTopic() == null || this.getOutputTopic().isEmpty())
			throw new RequestException("OutputTopic cannot be null or empty!");
	}

	private void validateDataFlowName() throws RequestException {
		if (this.getDataFlowName() == null || this.getDataFlowName().isEmpty())
			throw new RequestException("DataFlow Name cannot be null or empty!");
	}

	public boolean isValid() throws RequestException {
		this.validateInputSubscription();
		this.validateProject();
		this.validateStagingLocation();
		this.validateTempLocation();
		this.validateRunner();
		this.validategetGcsPath();
		this.validateOutputTopic();
		this.validateDataFlowName();
		return true;
	}

	public String toOptionsFormat() {
		return

				"--inputSubscription="+this.getInputSubscription()+" \\\n"+
						"--project="+this.getProject()+" \\\n"+
						"--stagingLocation="+this.getStagingLocation()+" \\\n"+
						"--tempLocation="+this.getTempLocation()+" \\\n"+
						"--runner="+this.getRunner()+" \\\n"+
						"--gcsFilePath="+this.getGcsPath()+" \\\n"+
						"--outputTopic="+this.getOutputTopic()+" \\\n"+
						"--dataFlowName="+this.getDataFlowName();
	}
}