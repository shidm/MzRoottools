package com.meizu.mzroottools.model.pojo;

public class GetRootCodeBackMessage {

    private String code;
    private String message;
    private CodeMessage value;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public CodeMessage getValue() {
        return value;
    }

    public void setValue(CodeMessage value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "GetRootCodeBackMessage{" +
                "code='" + code + '\'' +
                ", message='" + message + '\'' +
                ", value=" + value +
                '}';
    }

    public class CodeMessage {
        public CodeMessage() {
        }

        private String chipid;
        private String code;

        public String getChipid() {
            return chipid;
        }

        public void setChipid(String chipid) {
            this.chipid = chipid;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        @Override
        public String toString() {
            return "CodeMessage{" +
                    "chipid='" + chipid + '\'' +
                    ", code='" + code + '\'' +
                    '}';
        }
    }
}
