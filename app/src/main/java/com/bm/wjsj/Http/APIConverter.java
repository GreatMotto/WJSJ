package com.bm.wjsj.Http;

/**
 * Created by Nathan on 15/5/25.
 */

import com.google.gson.Gson;
import com.google.gson.JsonParseException;

import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit.converter.ConversionException;
import retrofit.converter.Converter;
import retrofit.mime.MimeUtil;
import retrofit.mime.MultipartTypedOutput;
import retrofit.mime.TypedFile;
import retrofit.mime.TypedInput;
import retrofit.mime.TypedOutput;
import retrofit.mime.TypedString;

/**
 * A {@link Converter} which uses GSON for serialization and deserialization of entities.
 *
 * @author Jake Wharton (jw@squareup.com)
 */
public class APIConverter<T> implements Converter {
    private final Gson gson;
    private String charset;

    private Class<T> mClass;

    /**
     * Create an instance using the supplied {@link Gson} object for conversion. Encoding to JSON and
     * decoding from JSON (when no charset is specified by a header) will use UTF-8.
     */
    public APIConverter(Gson gson, Class<T> parentClass) {
        this(gson, "UTF-8");
        mClass = parentClass;
    }

    /**
     * Create an instance using the supplied {@link Gson} object for conversion. Encoding to JSON and
     * decoding from JSON (when no charset is specified by a header) will use the specified charset.
     */
    public APIConverter(Gson gson, String charset) {
        this.gson = gson;
        this.charset = charset;
    }

    static ParameterizedType type(final Class<?> raw, final Type... args) {
        return new ParameterizedType() {

            public Type getRawType() {
                return raw;
            }

            public Type[] getActualTypeArguments() {
                return args;
            }

            public Type getOwnerType() {
                return null;
            }
        };
    }

    @Override
    public Object fromBody(TypedInput body, Type type) throws ConversionException {
        String charset = this.charset;
        if (body.mimeType() != null) {
            charset = MimeUtil.parseCharset(body.mimeType(), charset);
        }
        InputStreamReader isr = null;
        try {
            isr = new InputStreamReader(body.in(), charset);

            Type objectType;
            if (mClass != null) {
                objectType = type(APIResponse.class, mClass);
            } else {
                objectType = type;
            }
            return gson.fromJson(isr, objectType);
        } catch (IOException e) {
            throw new ConversionException(e);
        } catch (JsonParseException e) {
            throw new ConversionException(e);
        } finally {
            if (isr != null) {
                try {
                    isr.close();
                } catch (IOException ignored) {
                }
            }
        }
    }

    @Override
    public TypedOutput toBody(Object object) {
        try {
            MultipartTypedOutput multipartTypedOutput = new MultipartTypedOutput();
            // 处理map
            if(object instanceof HashMap) {
                Map<String, Object> params = (Map<String, Object>) object;
                for (String key : params.keySet()) {
                    if (params.get(key) instanceof String){
                        TypedString typedString = new TypedString(String.valueOf(params.get(key)));
                        multipartTypedOutput.addPart(key, typedString);
                    }else if (params.get(key) instanceof ArrayList) {
                        // 处理文件
                        List<TypedFile> fileList = (List<TypedFile>) params.get(key);
                        for (TypedFile file : fileList) {
                            multipartTypedOutput.addPart("path",file);
                        }
                    }
                }
            }
//            else if (object instanceof ArrayList) {
//                // 处理文件
//                List<TypedFile> fileList = (List<TypedFile>) object;
//                for (TypedFile file : fileList) {
//                    multipartTypedOutput.addPart("path",file);
//                }
//            }
            return multipartTypedOutput;

            //return new JsonTypedOutput(gson.toJson(object).getBytes(charset), charset);
        } catch (Exception e) {
            throw new AssertionError(e);
        }
    }

    /*
    private static class JsonTypedOutput implements TypedOutput {
        private final byte[] jsonBytes;
        private final String mimeType;

        JsonTypedOutput(byte[] jsonBytes, String encode) {
            this.jsonBytes = jsonBytes;
            this.mimeType = "multipart/form-data; charset=" + encode;
        }

        @Override
        public String fileName() {
            return null;
        }

        @Override
        public String mimeType() {
            return mimeType;
        }

        @Override
        public long length() {
            return jsonBytes.length;
        }

        @Override
        public void writeTo(OutputStream out) throws IOException {
            out.write(jsonBytes);
        }
    }
    */
}

