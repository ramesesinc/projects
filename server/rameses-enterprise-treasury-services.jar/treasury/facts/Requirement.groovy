package treasury.facts;

public class Requirement {
	
	String code;
	String title;
	String message;
	boolean completed;
	int sortorder;
	boolean required;

	def Map toMap() {
		return [
			code: this.code,
			title: this.title,
			message: this.message,
			completed: this.completed,
			sortorder: this.sortorder,
			required: this.required
		];	
	}

}