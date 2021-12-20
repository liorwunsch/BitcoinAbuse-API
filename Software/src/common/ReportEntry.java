package common;

import javafx.scene.control.Hyperlink;
import javafx.scene.image.ImageView;

public class ReportEntry {

	private String address;
	private String reportCount;
	private ImageView img;
	private Hyperlink link;

	public ReportEntry(String address, String report_count, String link) {
		this.address = address;
		this.reportCount = report_count;
		this.img = null;
		this.link = new Hyperlink(link);
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address_) {
		address = address_;
	}

	public String getReportCount() {
		return reportCount;
	}

	public void setReportCount(String reportCount_) {
		reportCount = reportCount_;
	}

	public ImageView getImg() {
		return img;
	}

	public void setImg(ImageView img_) {
		img = img_;
	}

	public Hyperlink getLink() {
		return link;
	}

	public void setLink(String link_) {
		link = new Hyperlink(link_);
	}
}
