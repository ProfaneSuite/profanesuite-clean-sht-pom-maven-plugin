package io.profanesuite.maven.plugins.clean.sht.model;

import io.profanesuite.maven.plugins.clean.sht.Builder;

import java.nio.file.Path;

public class LocationContext {

    /**
     * The internal location of the pom represented as a Path.
     */
    private final Path internalEffectivePomLocation;

    /**
     * The internal location of the pom represented as a Path.
     */
    private final Path internalProductionPomLocation;


    /**
     * Constructor used by the builder to create the context.
     *
     * @param effectivePomLocation pom location as a path
     * @param productionPomLocation pom location as a path
     */
    private LocationContext(final Path effectivePomLocation,
                            final Path productionPomLocation)
    {
        internalEffectivePomLocation = effectivePomLocation;
        internalProductionPomLocation = productionPomLocation;
    }

    public Path getEffectivePomLocation() {
        return internalEffectivePomLocation;
    }

    public Path getProductionPomLocation() {
        return internalProductionPomLocation;
    }

    public static class LocationContextBuilder implements Builder<LocationContext> {

        /**
         * Effective pom location as a String
         */
        private String internalEffectivePomLocation;
        private Path internalEffectivePomLocationPath;
        private String internalProductionPomLocation;
        private Path internalProductionPomLocationPath;


        /**
         *
         * @param effectivePomLocation Where the production pom will be located.
         * @return The context builder
         */
        public LocationContext.LocationContextBuilder withEffectivePomLocation(final String effectivePomLocation) {
            return withEffectivePomLocation(Path.of(effectivePomLocation));
        }

        public LocationContext.LocationContextBuilder withEffectivePomLocation(final Path effectivePomLocation) {
            internalEffectivePomLocationPath = effectivePomLocation.toAbsolutePath();
            this.internalEffectivePomLocation = internalEffectivePomLocationPath.toString();
            return this;
        }

        /**
         *
         * @param productionPomLocation Where the production pom will be located.
         * @return The context builder
         */
        public LocationContext.LocationContextBuilder withProductionPomLocation(final String productionPomLocation) {
            return withProductionPomLocation(Path.of(productionPomLocation));
        }

        public LocationContext.LocationContextBuilder withProductionPomLocation(final Path productionPomLocation) {
            internalProductionPomLocationPath = productionPomLocation.toAbsolutePath();
            this.internalProductionPomLocation = internalProductionPomLocationPath.toString();
            return this;
        }

        @Override
        public LocationContext build() {
            return new LocationContext(internalEffectivePomLocationPath, internalProductionPomLocationPath);
        }
    }

}
