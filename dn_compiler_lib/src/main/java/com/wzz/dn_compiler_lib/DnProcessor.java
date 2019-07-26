package com.wzz.dn_compiler_lib;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.wzz.dn_annotation_lib.DRouter;
import com.wzz.dn_annotation_lib.model.RouteMeta;
import com.wzz.dn_compiler_lib.utils.Log;
import com.wzz.dn_compiler_lib.utils.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;


@AutoService(Processor.class)
public class DnProcessor extends AbstractProcessor {

    /**
     *  1.告诉APT，我们需要处理哪些注解
     * 它是一个字符串的集合，意味着可以支持多个类型的注解，并且字符串是合法全名。
     * @return
     */
    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> types=new LinkedHashSet<>();
        types.add( DRouter.class.getCanonicalName());
        return types;
    }
    //2.添加支持的JDK的版本
    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    private Log log;
    /**
     * type(类信息)工具类
     */
    private Types typeUtil;

    /**
     * 节点工具类 (类、函数、属性都是节点)
     */
    private Elements elementsUtil;

    /**
     * 需要一个生成源文件的对象  并初始化它
     */
    private Filer mFiler;
    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        log = Log.newLog(processingEnvironment.getMessager());
        mFiler = processingEnvironment.getFiler();
        typeUtil = processingEnvironment.getTypeUtils();
        elementsUtil = processingEnvironment.getElementUtils();

    }


    /**
     *
     * @param annotations
     * @param roundEnv
     * @return 如果返回 true，则这些注解已声明并且不要求后续 Processor 处理它们（一个元素多个注解，后面的注解将不可用）；
     *         如果返回 false，则这些注解未声明并且可能要求后续 Processor 处理它们
     */
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

        log.i("process =============" );

        //获取所有被Route 注解的元素集合
        if ( Utils.isEmpty( annotations )){
            return false ;
        }

        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(DRouter.class);
        if ( !Utils.isEmpty( elements ) ){
            log.i("Route Class: ===" + elements.size());
            parseRoutes( elements );
            return true ;
        }


        return false ;
    }

    //        public class Snake{        //TypeElement
//            private int a;         //VariableElement
//            private Snake other;   //VariableElement
//            public Snake(){}       //ExecuteableElement
//            public void method(){} //ExecuteableElement
//        }
    private void parseRoutes(Set<? extends Element> elements) {

        //elementsUtils 通过节点工具，传入全类名，生成节点 （activity）
        TypeElement typeElement = elementsUtil.getTypeElement("android.app.Activity");
        //节点的描述信息
        TypeMirror type_activity = typeElement.asType();

        for (Element element : elements) {

            TypeMirror type = element.asType();
            RouteMeta routeMeta ;

            if ( typeUtil.isSubtype( type , type_activity )){

                DRouter dRouter = element.getAnnotation(DRouter.class);

                routeMeta = new RouteMeta( RouteMeta.Type.ACTIVITY ,  dRouter , element );

                log.i("class:" + ClassName.get((TypeElement) element) + "   className:" + element.getSimpleName() + "  path:"+ dRouter.path()  );  // className:MainActivity  path:/main/main

            }else {
                throw new RuntimeException("[Just Support Activity/IService Route]:" + element);
            }

            /**
             * 根据注解上的path值 进行分组
             */
            categories(routeMeta);

        }

        TypeElement type = elementsUtil.getTypeElement("com.wzz.commonlib.dn_router.IRouteGroup"); //com.dn_alan.router_core.template.IRouteGroup
        /**
         *生成Group类
         */
        generatedGroup( type );

        TypeElement type_Root = elementsUtil.getTypeElement("com.wzz.commonlib.dn_router.IRouteRoot"); //com.dn_alan.router_core.template.IRouteGroup
        /**
         * 生成Root类
         */
        generatedRoot(type_Root);


    }

//    public class ARouter$$Root$$logincomponent implements IRouteRoot {
//        @Override
//        public void loadInto(Map<String, Class<? extends IRouteGroup>> routes) {
//            routes.put("login", ARouter$$Group$$login.class);
//            routes.put("login1", ARouter$$Group$$login1.class);
//        }
//    }

    /**
     * 生成Root类 -->  用于注册每个group和group对应的class类
     *
     * @param type_Root
     */
    private void generatedRoot(TypeElement type_Root) {

        /**
         *   @Override
         *   public void loadInto( Map<String, Class<? extends IRouteGroup>> routes ) {}
         *
         * 1.创建参数类型  Map<String, Class<? extends IRouteGroup>>
         */
        ParameterizedTypeName typeName = ParameterizedTypeName.get(
                ClassName.get(Map.class),
                ClassName.get(String.class),
                ClassName.get(Class.class)); // todo 这里需要extends 完善

        /**
         * 2. 创建参数名字   Map<String, Class<? extends IRouteGroup>> routes
         */

        ParameterSpec spec = ParameterSpec.builder(typeName, "routes").build();

        /**
         *  遍历分组 根据每个组生成一个Group类  如：DnRouter$$Root$$app
         */
        Set<Map.Entry<String, List<RouteMeta>>> entrySet = map.entrySet();

        /**
         *  3.生成方法
         *   @Override
         *   public void loadInto(Map<String, Class<? extends IRouteGroup>> routes) {}
         */
        MethodSpec.Builder builder = MethodSpec.methodBuilder("loadInto")  // 方法名
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .returns(TypeName.VOID)
                .addParameter(spec);

        for (Map.Entry<String, List<RouteMeta>> entry : entrySet) {

            String groupName = entry.getKey();

            /**
             * 4.根据组名  循环添加方法体中的方法
             *   routes.put("main", DnRouter$$Group$$main.class );
             *   routes.put("login", ARouter$$Group$$login.class);
             */
            builder.addStatement("routes.put($S, $T.class )",
                    groupName ,
                    ClassName.get( "com.wzz.route" ,  "DnRouter$$Group$$" + groupName ) );

        }

        try {

            /**
             *  5.生成类名
             *  DnRouter$$Root$$app
             */

            // "DnRouter$$Root$$
            String RootclassName = "DnRouter" + "$$" + "Root" + "$$" + "app" ;  // todo 这个app需要根据每个module的不同进行修改
            JavaFile.builder("com.wzz.route" ,
                    TypeSpec.classBuilder( RootclassName )
                            .addSuperinterface( ClassName.get( type_Root ))
                            .addModifiers( Modifier.PUBLIC)
                            .addMethod( builder.build() )
                            .build() )
                    .build()
                    .writeTo( mFiler);
        }catch (Exception e){
            log.i( "exception:"+e.getMessage());
            e.printStackTrace();
        }

    }

    /**
     *
     * 生成Group类：
     *
     * public class DnRouter$$Group$$main implements IRouteGroup {
     *   @Override
     *   public void loadInto(Map<String, RouteMeta> atlas) {
     *     atlas.put("/main/b",RouteMeta.build(RouteMeta.Type.ACTIVITY, BActivity.class, "/main/b" , "main" ) );
     *     atlas.put("/main/main",RouteMeta.build(RouteMeta.Type.ACTIVITY, MainActivity.class, "/main/main" , "main" ) );
     *   }
     * }
     * @param typeElement  group类要实现的接口
     */
    private void generatedGroup( TypeElement typeElement) {
        //创建参数类型  Map<MapString, RouteMeta>
        /**
         *   @Override
         *   public void loadInto(Map<String, RouteMeta> atlas) {}
         *
         * 1.创建参数类型  Map<String, RouteMeta>
         */
        ParameterizedTypeName typeName = ParameterizedTypeName.get(
                ClassName.get(Map.class),
                ClassName.get(String.class),
                ClassName.get(RouteMeta.class));
        /**
         * 2. 创建参数名字  Map<String, RouteMeta> atlas
         */
        ParameterSpec spec = ParameterSpec.builder(typeName, "atlas").build();

        /**
         *  遍历分组 根据每个组生成一个Group类  如：DnRouter$$Group$$main、DnRouter$$Group$$movie
         */
        Set<Map.Entry<String, List<RouteMeta>>> entrySet = map.entrySet();

        for (Map.Entry<String, List<RouteMeta>> entry : entrySet) {

            /**
             *  3.生成方法
             *   @Override
             *   public void loadInto(Map<String, RouteMeta> atlas) {}
             */
            MethodSpec.Builder builder = MethodSpec.methodBuilder("loadInto")
                    .addAnnotation(Override.class) // 注解
                    .addModifiers(Modifier.PUBLIC) // 公共的
                    .returns(TypeName.VOID) // 返回void
                    .addParameter(spec);


            String groupName = entry.getKey();
            List<RouteMeta> groupData = entry.getValue();

            for (RouteMeta routeMeta : groupData) {

                /**
                 * 4.根据每个组中的内容  循环添加方法体中的方法
                 *   atlas.put("/main/main",RouteMeta.build(RouteMeta.Type.ACTIVITY, MainActivity.class, "/main/main" , "main" ) );
                 *   atlas.put("/main/b",RouteMeta.build(RouteMeta.Type.ACTIVITY, BActivity.class, "/main/b" , "main" ) );
                 */
                builder.addStatement("atlas.put($S,$T.build($T.$L, $T.class, $S , $S ) )",
                        routeMeta.getPath(),
                        ClassName.get(RouteMeta.class),
                        ClassName.get(RouteMeta.Type.class),
                        routeMeta.getType(), // RouteMeta.Type.ACTIVITY
                        ClassName.get( (TypeElement) routeMeta.getElement()), // SecondActivity.class
                        routeMeta.getPath().toLowerCase(),  // "/main/test"
                        routeMeta.getGroup().toLowerCase() ); // "main"

            }

            /**
             *  5.生成类名
             *  DnRouter$$Group$$main 、 DnRouter$$Group$$movie
             */
            String className = "DnRouter" + "$$" + "Group" + "$$" + groupName ;

            try {
                JavaFile.builder("com.wzz.route" ,
                        TypeSpec.classBuilder( className )
                                .addSuperinterface( ClassName.get(typeElement)) // 实现的接口
                                .addModifiers( Modifier.PUBLIC)
                                .addMethod( builder.build() )
                                .build() )
                        .build()
                        .writeTo( mFiler);
            }catch (IOException e){
                e.printStackTrace();
            }

        }


    }

    /**
     * 此map存放 组和组中RouteMeta集合，
     * 如：有三个模块： main，login，movie ，每个模块中有很多的添加了注解的类
     * { "main": <A,B,C>  , "login":<D,E> , "movie":< F > }
     */
    Map<String, List<RouteMeta> > map = new HashMap<>() ;

    private void categories(RouteMeta routeMeta) {
        // 先校验定义的path值是否合格
        if ( routeVerify( routeMeta ) ){
            String group = routeMeta.getGroup();
            log.i("group:" + group );
            List<RouteMeta> list = map.get( group );
            // 如果为空，先创建list，再添加
            if ( Utils.isEmpty( list ) ){
                list = new ArrayList<>( );
                list.add( routeMeta );
                map.put( group , list);
            }else {
                // 如果不为空，说明之前添加过此组
                list.add( routeMeta ) ;
            }
        }else {
            log.i("Path Define Error :" + routeMeta.getPath() + "   _wzz");
            // 如果注解值不符合标准 抛出异常： [Path Define Error]: CActivity: /main
            throw new RuntimeException("[Path Define Error]: " + routeMeta.getElement().getSimpleName() + ": " +routeMeta.getPath() );
        }

        log.i("map:" + map );
    }

    /**
     * 校验定义的@DRouter(path = "/main/login") 中的path和group的值 ： 如果定义的group为空，则则截取path中/main/login的main为group的值
     * @param routeMeta
     * @return
     */
    private boolean routeVerify(RouteMeta routeMeta) {
        String path = routeMeta.getPath(); // "/main/login"
        String group = routeMeta.getGroup();
        if ( Utils.isEmpty( path ) || !path.startsWith("/") ){ // 如果给定的path不合格 则返回false

            return false ;
        }
        if ( Utils.isEmpty( group ) ){
            String path_group = "";
            try {
                path_group = path.substring(1, path.indexOf("/", 1));// 如果没定义group，则截取path中/main/login的main为group的值
            }catch (Exception e){
                return false ;
            }

            if ( Utils.isEmpty(path_group)){
                return false ;
            }
            routeMeta.setGroup( path_group ); // 把path中截取的值作为group
            return true ;
        }
        return true;
    }
}
