package io.profanesuite.maven.plugins.clean.sht.pom;

public enum Element {
    MODEL_VERSION("modelVersion"),
    GROUP_ID("groupId"),
    ARTIFACT_ID("artifactId"),
    VERSION("version"),
    PACKAGING("packaging"),
    DEPENDENCIES("dependencies"),
    PARENT("parent"),
    DEPENDENCY_MANAGEMENT("dependencyManagement"),
    MODULES("modules"),
    PROPERTIES("properties"),
    BUILD("build"),
    REPORTING("reporting"),
    NAME("name"),
    DESCRIPTION("description"),
    URL("url"),
    INCEPTION_YEAR("inceptionYear"),
    LICENSES("licenses"),
    ORGANIZATION("organization"),
    DEVELOPERS("developers"),
    CONTRIBUTORS("contributors"),
    ISSUE_MANAGEMENT("issueManagement"),
    CI_MANAGEMENT("ciManagement"),
    MAILING_LISTS("mailingLists"),
    SCM("scm"),
    PREREQUISITES("prerequisites"),
    REPOSITORIES("repositories"),
    PLUGIN_REPOSITORIES("pluginRepositories"),
    DISTRIBUTION_MANAGEMENT("distributionManagement"),
    PROFILES("profiles");

    public final static Element[] DEFAULT_INCLUDE = {
            MODEL_VERSION,
            GROUP_ID,
            ARTIFACT_ID,
            VERSION,
            PACKAGING,
            DEPENDENCIES,
            PROPERTIES,
            NAME,
            DESCRIPTION,
            URL,
            INCEPTION_YEAR,
            LICENSES,
            ORGANIZATION,
            DEVELOPERS,
            CONTRIBUTORS,
            ISSUE_MANAGEMENT,
            CI_MANAGEMENT,
            MAILING_LISTS,
            SCM,
            PREREQUISITES

    };

    public final static Element[] DEFAULT_EXCLUDE = {
            PARENT,
            DEPENDENCY_MANAGEMENT,
            MODULES,
            BUILD,
            REPORTING,
            REPOSITORIES,
            PLUGIN_REPOSITORIES,
            DISTRIBUTION_MANAGEMENT,
            PROFILES
    };

    private final String elementName;

    Element(final String elementName) {

        this.elementName = elementName;
    }

    public String getElementName() {
        return elementName;
    }
}
