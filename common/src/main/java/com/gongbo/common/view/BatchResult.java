package com.gongbo.common.view;

import cn.hutool.core.collection.CollUtil;
import com.google.common.collect.Sets;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
@Setter
@ToString
@ApiModel
public class BatchResult<T> {
    @ApiModelProperty("成功集合")
    private Set<T> succeeded;

    @ApiModelProperty("失败集合")
    private Set<T> failed;

    public static class Builder<T> {

        private Set<T> operations;

        private Set<T> succeeded;

        private Set<T> failed;

        public Builder<T> succeeded(Collection<T> data) {
            this.succeeded = toData(data);
            return this;
        }

        public Builder<T> failed(Collection<T> data) {
            this.failed = toData(data);
            return this;
        }

        public Builder<T> operations(Collection<T> data) {
            this.operations = toData(data);
            return this;
        }

        private Set<T> toData(Collection<T> data) {
            if (CollUtil.isEmpty(data)) {
                return Collections.emptySet();
            }
            return Sets.newHashSet(data);
        }

        private <T2> Set<T> toData(Collection<T2> data, Function<T2, T> mapper) {
            if (CollUtil.isEmpty(data)) {
                return Collections.emptySet();
            }
            return data.stream()
                    .map(mapper).collect(Collectors.toSet());
        }

        public <T2> Builder<T> succeeded(Collection<T2> data, Function<T2, T> mapper) {
            this.succeeded = toData(data, mapper);
            return this;
        }

        public <T2> Builder<T> failed(Collection<T2> data, Function<T2, T> mapper) {
            this.failed = toData(data, mapper);
            return this;
        }

        public <T2> Builder<T> operations(Collection<T2> data, Function<T2, T> mapper) {
            this.operations = toData(data, mapper);
            return this;
        }

        public BatchResult<T> build() {
            BatchResult<T> batchResult = new BatchResult<>();

            if (succeeded == null && operations != null && failed != null) {
                operations.removeAll(failed);
                succeeded = operations;
            }

            if (failed == null && operations != null && succeeded != null) {
                operations.removeAll(succeeded);
                failed = operations;
            }

            if (succeeded == null) {
                succeeded = Collections.emptySet();
            }
            if (failed == null) {
                failed = Collections.emptySet();
            }
            batchResult.setSucceeded(succeeded);
            batchResult.setFailed(failed);

            return batchResult;
        }

    }
}
