package com.chapterly.records;

import java.io.Serializable;
import java.util.List;

public record SearchRecord(List<String> searchResults) implements Serializable {
}
