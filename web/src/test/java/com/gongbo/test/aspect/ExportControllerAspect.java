//package com.gongbo.test.aspect;
//
//import lombok.extern.slf4j.Slf4j;
//import org.aspectj.lang.JoinPoint;
//import org.aspectj.lang.annotation.AfterReturning;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Pointcut;
//import org.aspectj.lang.reflect.MethodSignature;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Component;
//import org.springframework.web.util.WebUtils;
//
//import javax.servlet.http.HttpServletRequest;
//import java.lang.reflect.Method;
//
///**
// * 针对Controller的响应结果进行检查
// */
//@Component
//@Aspect
//@Slf4j
//public class ExportControllerAspect {
//
//
//    @Pointcut("@annotation(com.goldwind.pf.common.export.annotations.EnableExport) || @annotation(com.goldwind.pf.common.export.annotations.EnableExports)")
//    public void exportQuery() {
//    }
//
//    @AfterReturning(value = "exportQuery()", returning = "result")
//    public void aspectAfter(JoinPoint joinPoint, ResponseEntity<?> result) {
//        if (ExportContextHolder.isExportExcel()) {
//            return;
//        }
//
//        HttpServletRequest request = WebUtils.getRequest();
//        if (request != null && request.getParameter("export") != null) {
//            return;
//        }
//
//        //获取代理方法
//        Method targetMethod = getTargetMethod(joinPoint);
//
//        ExportParam exportParam = ExportParam.builder()
//                .type(ExportParam.Type.EXPORT_EXCEL)
//                .build();
//
//        try {
//            //构建导出上下文
//            ExportContext exportContext = ExportHelper.buildExportContext(exportParam, targetMethod);
//
//            //数据转换
//            ExportDataConvert exportDataConvert = ExportHandlers.of(exportContext.getEnableExport().dataConvert());
//            exportDataConvert.convert(exportContext, result);
//
//            AfterExportHandler afterExportHandler = ExportHandlers.of(exportContext.getEnableExport().afterExportHandler());
//            //导出文件后执行
//            afterExportHandler.afterExport(exportContext);
//        } catch (Exception e) {
//            log.error(e.getMessage());
//        }
//    }
//
//    /**
//     * 获取当前请求执行方法
//     */
//    private static Method getTargetMethod(JoinPoint joinPoint) {
//        return ((MethodSignature) joinPoint.getSignature()).getMethod();
//    }
//}
