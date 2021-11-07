package com.undertheriver.sgsg.foler.domain;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Sort;

import com.undertheriver.sgsg.foler.repository.FolderRepository;

public enum FolderOrderBy {
    DEFAULT {
        @Override
        public Sort getSort() {
            return Sort.by(Sort.Direction.ASC, "createdAt");
        }
    },
    NAME {
        @Override
        public Sort getSort() {
            return Sort.by(Sort.Direction.ASC, "title");
        }
    },
    CREATED_AT {
        @Override
        public Sort getSort() {
            return Sort.by(Sort.Direction.ASC, "createdAt");
        }
    },
    MEMO {
        @Override
        public Sort getSort() {
            return Sort.by(Sort.Direction.ASC, "createdAt");
        }
    };

    abstract public Sort getSort();
}
