package lule.dictionary.service.imports.importService.dto;

public record FormPositionData(String left,
                               String right,
                               String top,
                               String bottom) {
    public FormPositionData {
        if(left == null) left = "";
        if(right == null) right = "";
        if(top == null) top = "";
        if(bottom == null) bottom = "";
    }

    public static FormPositionData of(String left, String right, String top, String bottom) {
        return new FormPositionData(left, right, top, bottom);
    }

    @Override
    public String left() {
        if(left == null) return "";
        return left;
    }

    @Override
    public String right() {
        if(right == null) return "";
        return right;
    }

    @Override
    public String top() {
        if(top == null) return "";
        return top;
    }

    @Override
    public String bottom() {
        if(bottom == null) return "";
        return bottom;
    }
}
