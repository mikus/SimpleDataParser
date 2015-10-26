package net.mikus.assignment.sdp


class Utils {

    static InputStream toStream(final String txt) {
        new ByteArrayInputStream(txt.bytes)
    }

}
