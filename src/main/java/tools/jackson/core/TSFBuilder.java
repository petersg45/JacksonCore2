package tools.jackson.core;

import java.util.Objects;

import tools.jackson.core.util.BufferRecyclerPool;

/**
 * Since factory instances are immutable, a Builder class is needed for creating
 * configurations for differently configured factory instances.
 */
public abstract class TSFBuilder<F extends TokenStreamFactory,
    B extends TSFBuilder<F,B>>
{
    /**
     * Set of {@link TokenStreamFactory.Feature}s enabled, as bitmask.
     */
    protected int _factoryFeatures;

    /**
     * Set of {@link StreamReadFeature}s enabled, as bitmask.
     */
    protected int _streamReadFeatures;

    /**
     * Set of {@link StreamWriteFeature}s enabled, as bitmask.
     */
    protected int _streamWriteFeatures;

    /**
     * Set of format-specific read {@link FormatFeature}s enabled, as bitmask.
     */
    protected int _formatReadFeatures;

    /**
     * Set of format-specific write {@link FormatFeature}s enabled, as bitmask.
     */
    protected int _formatWriteFeatures;

    /**
     * Buffer recycler provider to use.
     */
    protected BufferRecyclerPool _bufferRecyclerPool;
    
    /**
     * StreamReadConstraints to use.
     */
    protected StreamReadConstraints _streamReadConstraints;

    /**
     * StreamWriteConstraints to use.
     */
    protected StreamWriteConstraints _streamWriteConstraints;

    /**
     * {@link ErrorReportConfiguration} to use.
     */
    protected ErrorReportConfiguration _errorReportConfiguration;    

    // // // Construction

    protected TSFBuilder(StreamReadConstraints src, StreamWriteConstraints swc,
            ErrorReportConfiguration erc,
            int formatReadF, int formatWriteF) {
        this(BufferRecyclerPool.defaultPool(),
                src, swc, erc,
                TokenStreamFactory.DEFAULT_FACTORY_FEATURE_FLAGS,
                TokenStreamFactory.DEFAULT_STREAM_READ_FEATURE_FLAGS,
                TokenStreamFactory.DEFAULT_STREAM_WRITE_FEATURE_FLAGS,
                formatReadF, formatWriteF);
    }

    protected TSFBuilder(TokenStreamFactory base)
    {
        this(base._bufferRecyclerPool,
                base._streamReadConstraints, base._streamWriteConstraints,
                base._errorReportConfiguration,
                base._factoryFeatures,
                base._streamReadFeatures, base._streamWriteFeatures,
                base._formatReadFeatures, base._formatWriteFeatures);
    }

    protected TSFBuilder(BufferRecyclerPool brp,
            StreamReadConstraints src, StreamWriteConstraints swc,
            ErrorReportConfiguration erc,
            int factoryFeatures,
            int streamReadFeatures, int streamWriteFeatures,
            int formatReadFeatures, int formatWriteFeatures)
    {
        _bufferRecyclerPool = Objects.requireNonNull(brp);
        _streamReadConstraints = Objects.requireNonNull(src);
        _streamWriteConstraints = Objects.requireNonNull(swc);
        _errorReportConfiguration = Objects.requireNonNull(erc);
        _factoryFeatures = factoryFeatures;
        _streamReadFeatures = streamReadFeatures;
        _streamWriteFeatures = streamWriteFeatures;
        _formatReadFeatures = formatReadFeatures;
        _formatWriteFeatures = formatWriteFeatures;
    }

    // // // Accessors

    public int factoryFeaturesMask() { return _factoryFeatures; }
    public int streamReadFeaturesMask() { return _streamReadFeatures; }
    public int streamWriteFeaturesMask() { return _streamWriteFeatures; }

    public int formatReadFeaturesMask() { return _formatReadFeatures; }
    public int formatWriteFeaturesMask() { return _formatWriteFeatures; }

    public BufferRecyclerPool bufferRecyclerPool() {
        return _bufferRecyclerPool;
    }
    
    // // // Factory features

    public B enable(TokenStreamFactory.Feature f) {
        _factoryFeatures |= f.getMask();
        return _this();
    }

    public B disable(TokenStreamFactory.Feature f) {
        _factoryFeatures &= ~f.getMask();
        return _this();
    }

    public B configure(TokenStreamFactory.Feature f, boolean state) {
        return state ? enable(f) : disable(f);
    }

    // // // Parser features

    public B enable(StreamReadFeature f) {
        _streamReadFeatures |= f.getMask();
        return _this();
    }

    public B enable(StreamReadFeature first, StreamReadFeature... other) {
        _streamReadFeatures |= first.getMask();
        for (StreamReadFeature f : other) {
            _streamReadFeatures |= f.getMask();
        }
        return _this();
    }

    public B disable(StreamReadFeature f) {
        _streamReadFeatures &= ~f.getMask();
        return _this();
    }

    public B disable(StreamReadFeature first, StreamReadFeature... other) {
        _streamReadFeatures &= ~first.getMask();
        for (StreamReadFeature f : other) {
            _streamReadFeatures &= ~f.getMask();
        }
        return _this();
    }

    public B configure(StreamReadFeature f, boolean state) {
        return state ? enable(f) : disable(f);
    }

    // // // Generator features

    public B enable(StreamWriteFeature f) {
        _streamWriteFeatures |= f.getMask();
        return _this();
    }

    public B enable(StreamWriteFeature first, StreamWriteFeature... other) {
        _streamWriteFeatures |= first.getMask();
        for (StreamWriteFeature f : other) {
            _streamWriteFeatures |= f.getMask();
        }
        return _this();
    }

    public B disable(StreamWriteFeature f) {
        _streamWriteFeatures &= ~f.getMask();
        return _this();
    }

    public B disable(StreamWriteFeature first, StreamWriteFeature... other) {
        _streamWriteFeatures &= ~first.getMask();
        for (StreamWriteFeature f : other) {
            _streamWriteFeatures &= ~f.getMask();
        }
        return _this();
    }

    public B configure(StreamWriteFeature f, boolean state) {
        return state ? enable(f) : disable(f);
    }

    // // // Other configuration, constraints

    /**
     * Sets the constraints for streaming reads.
     *
     * @param streamReadConstraints constraints for streaming reads
     * @return this builder
     */
    public B streamReadConstraints(StreamReadConstraints streamReadConstraints) {
        _streamReadConstraints = streamReadConstraints;
        return _this();
    }

    /**
     * Sets the constraints for streaming writes.
     *
     * @param streamWriteConstraints constraints for streaming writes
     *
     * @return this builder
     */
    public B streamWriteConstraints(StreamWriteConstraints streamWriteConstraints) {
        _streamWriteConstraints = streamWriteConstraints;
        return _this();
    }

    /**
     * Sets the configuration for error tokens.
     *
     * @param errorReportConfiguration configuration values used for handling errorneous token inputs. 
     *
     * @return this builder
     */
    public B errorReportConfiguration(ErrorReportConfiguration errorReportConfiguration) {
        _errorReportConfiguration = errorReportConfiguration;
        return _this();
    }

    // // // Other configuration, helper objects

    /**
     * @param p BufferRecyclerPool to use for buffer allocation
     *
     * @return this builder (for call chaining)
     */
    public B bufferRecyclerPool(BufferRecyclerPool p) {
        _bufferRecyclerPool = Objects.requireNonNull(p);
        return _this();
    }

    // // // Other methods

    /**
     * Method for constructing actual {@link TokenStreamFactory} instance, given
     * configuration.
     *
     * @return {@link TokenStreamFactory} build using builder configuration settings
     */
    public abstract F build();

    // silly convenience cast method we need
    @SuppressWarnings("unchecked")
    protected final B _this() { return (B) this; }
}
