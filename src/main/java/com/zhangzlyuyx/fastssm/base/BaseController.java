package com.zhangzlyuyx.fastssm.base;

import java.beans.PropertyEditorSupport;
import java.lang.reflect.ParameterizedType;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.HtmlUtils;

import com.zhangzlyuyx.fastssm.mybatis.PageQuery;
import com.zhangzlyuyx.fastssm.util.ControllerUtils;
import com.zhangzlyuyx.fastssm.util.DateUtils;
import com.zhangzlyuyx.fastssm.util.StringUtils;

public abstract class BaseController<T> {

	public Class<T> getTargetType(){
		Class<T> classz = (Class<T>)((ParameterizedType)this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
		return classz;
	}
	
	public String getTargetName() {
		String name = this.getTargetService().getEntityClass().getName();
		return name.substring(0, 1).toLowerCase() + name.substring(1);
	}
	
	public abstract BaseService<T> getTargetService();
	
	@InitBinder
    public void initBinder(ServletRequestDataBinder binder) {
		//自动转换日期类型的字段格式
		binder.registerCustomEditor(Date.class, new PropertyEditorSupport() {
        	@Override
        	public void setAsText(String text) throws IllegalArgumentException {
        		if(StringUtils.isEmpty(text)) {
        			super.setValue(null);
        		} else {
        			Date value = DateUtils.parse(text, "yyyy-MM-dd HH:mm:ss");
        			super.setValue(value);
        		}
        	}
        	
        	@Override
        	public String getAsText() {
        		Object value = super.getValue();
        		if(value == null) {
        			return "";
        		}
        		Date date = (Date)value;
        		return DateUtils.format(date, "yyyy-MM-dd HH:mm:ss");
        	}
        });
		
		//防止XSS攻击
		binder.registerCustomEditor(String.class, new PropertyEditorSupport() {
        	@Override
        	public void setAsText(String text) throws IllegalArgumentException {
        		if(text == null) {
        			super.setValue(null);
        		} else {
        			String value = HtmlUtils.htmlEscape(text);
        			super.setAsText(value);
        		}
        	}
        	
        	@Override
        	public String getAsText() {
        		Object value = super.getValue();
        		return value != null ? value.toString() : "";
        	}
        });
	}
	
	/**
	 * 获取列表页url
	 * @return
	 */
	public String getListPageUrl() {
		String targetName = this.getTargetName();
		return targetName + "/" + targetName;
	}
	
	/**
	 * 显示列表页
	 * @param request
	 * @return
	 */
	@RequestMapping("/showList")
	public String showList(HttpServletRequest request){
		return this.getListPageUrl();
	}
	
	/**
	 * 获取列表
	 * @param request
	 * @param pageQuery
	 * @return
	 */
	@RequestMapping("/list")
	@ResponseBody
	public Object list(HttpServletRequest request, PageQuery pageQuery) {
		return this.getTargetService().select(pageQuery);
	}
	
	/**
	 * 获取添加页url
	 * @return
	 */
	public String getAddPageUrl() {
		String targetName = this.getTargetName();
		return targetName + "/" + targetName + "Add";
	}
	
	/**
	 * 显示添加页
	 * @param request
	 * @return
	 */
	@RequestMapping("/addPage")
	public String showAddPage(HttpServletRequest request){
		return this.getAddPageUrl();
	}
	
	/**
	 * 保存
	 * @param request
	 * @param entity
	 * @return
	 */
	@RequestMapping("/save")
	@ResponseBody
	public Object save(HttpServletRequest request, T entity){
		this.getTargetService().insertSelective(entity);
		return ControllerUtils.returnSuccess(entity, "添加成功!");
	}
	
	/**
	 * 获取编辑页url
	 * @return
	 */
	public String getEditPageUrl() {
		String targetName = this.getTargetName();
		return targetName + "/" + targetName + "Edit";
	}
	
	/**
	 * 显示编辑页
	 * @param request
	 * @param id
	 * @return
	 */
	@RequestMapping("/editPage")
	public String showEditPage(HttpServletRequest request, Long id){
		request.setAttribute("id", id);
		T entity = this.getTargetService().selectByPrimaryKey(id);
		request.setAttribute("entity", entity);
		return getEditPageUrl();
	}
	
	/**
	 * 更新
	 * @param request
	 * @param entity
	 * @return
	 */
	@RequestMapping("/update")
	@ResponseBody
	public Object update(HttpServletRequest request, T entity) {
		if(this.getTargetService().updateSelective(entity) > 0) {
			return ControllerUtils.returnSuccess(entity, "保存成功!");
		}else {
			return ControllerUtils.returnFail("保存失败!");
		}
	}
	
	/**
	 * 删除
	 * @param request
	 * @param ids
	 * @return
	 */
	@RequestMapping("/delete")
	@ResponseBody
	public Object delete(HttpServletRequest request, @RequestParam(value = "ids") Long[] ids) {
		if(ids == null || ids.length == 0) {
			return ControllerUtils.returnFail("请选择需要删除的记录!");
		}
		int count = 0;
		for(Long id : ids) {
			count += this.getTargetService().deleteByPrimaryKey(id);
		}
		if(count > 0) {
			return ControllerUtils.returnSuccess(count, "删除成功!");
		}else {
			return ControllerUtils.returnFail("删除失败!");
		}
	}
}
