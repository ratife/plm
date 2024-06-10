package plm.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Column;

@Entity
public class VersionSchema {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String versionTemplate;

    public VersionSchema() {
        this.versionTemplate = "MAJOR.MINOR.PATCH"; 
    }

    public VersionSchema(String versionTemplate) {
        this.versionTemplate = versionTemplate;
    }

    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getVersionTemplate() {
		return versionTemplate;
	}

	public void setVersionTemplate(String versionTemplate) {
		this.versionTemplate = versionTemplate;
	}
	
	 private String generateVersionLabel(int major, int minor, int patch) {
        String version = versionTemplate;
        version = version.replace("MAJOR", Integer.toString(major));
        version = version.replace("MINOR", Integer.toString(minor));
        version = version.replace("PATCH", Integer.toString(patch));
        return version;
    }

    public String getNextVersionLabel(String currentVersionLabel) {
        if (currentVersionLabel == null || currentVersionLabel.isEmpty()) {
            return generateVersionLabel(0, 0, 0);
        }

        String[] parts = currentVersionLabel.split("\\.");
        if (parts.length != 3) {
            throw new IllegalArgumentException("Invalid version label: " + currentVersionLabel);
        }

        try {
            int major = Integer.parseInt(parts[0]);
            int minor = Integer.parseInt(parts[1]);
            int patch = Integer.parseInt(parts[2]);
            patch++; 
            if (patch >= 10) {
                patch = 0;
                minor++;
                if (minor >= 10) {
                    minor = 0;
                    major++; 
                }
            }

            return generateVersionLabel(major, minor, patch);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid version label: " + currentVersionLabel, e);
        }
    }
}
