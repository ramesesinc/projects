package enterprise.facts;

public class Requirement {
	
	String code;
	String title;
	String message;
	boolean completed;
	int sortorder = 0;
	boolean required;
	

	def Map toMap() {
		return [
			reftype: this.code,
			code: this.code,
			title: this.title,
			message: this.message,
			completed: this.completed,
			sortorder: this.sortorder,
			required: this.required
		];	
	}

}