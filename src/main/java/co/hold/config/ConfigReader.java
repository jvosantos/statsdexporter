package co.hold.config;

import reactor.core.publisher.Mono;
import reactor.util.Logger;
import reactor.util.Loggers;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.CompletionHandler;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Optional;

public class ConfigReader {

    private static final String DEFAULT_PATH = "/tmp/config.json";
    private static final Logger LOGGER = Loggers.getLogger(ConfigReader.class.getName());

    public static Mono<byte[]> read(final String path) throws IOException {
        final String thePath = Optional.ofNullable(path).orElse(DEFAULT_PATH);

        AsynchronousFileChannel fileChannel = AsynchronousFileChannel.open(Paths.get(thePath), StandardOpenOption.READ);
        final long fileSize = fileChannel.size();

        LOGGER.debug(String.format("Reading configuration file %s with size %s", thePath, fileSize));

        ByteBuffer buffer = ByteBuffer.allocate((int) fileSize);

        return Mono.create(sink -> fileChannel.read(buffer, 0, buffer, new CompletionHandler<Integer, ByteBuffer>() {

            @Override
            public void completed(Integer result, ByteBuffer attachment) {

                //Haven't read everything
                if (fileSize > buffer.position()) {
                    buffer.clear();

                    //use the same completion handler for the next read
                    fileChannel.read(buffer, buffer.position(), buffer, this);
                } else {
                    // reached the end of file, return value
                    buffer.flip();
                    byte[] data = buffer.array();
                    buffer.clear();

                    sink.success(data);
                }
            }

            @Override
            public void failed(Throwable e, ByteBuffer attachment) {
                LOGGER.error("Error reading configuration file.", e);
                sink.error(e);
            }
        }));
    }
}
