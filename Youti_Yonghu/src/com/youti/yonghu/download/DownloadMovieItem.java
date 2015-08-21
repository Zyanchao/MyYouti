/**   
 * @Title: Download.java
 * @Package com.cloud.coupon.domain
 * @Description: TODO(用一句话描述该文件做什么)
 * @author 陈红建
 * @date 2013-7-3 下午5:52:46
 * @version V1.0
 */
package com.youti.yonghu.download;

import java.io.Serializable;

import net.tsz.afinal.annotation.sqlite.Table;

/**
 * @ClassName: Download
 * @Description:下载对象的封装 , 包括下载信息,和电影信息,用于下载列表中
 * @author 陈红建
 * @date 2013-7-3 下午5:52:46
 * 
 */
@Table(name="downloadtask")//用于FinalDb指定的表名
public class DownloadMovieItem implements Serializable{

	/**
	 * @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么)
	 */
	private static final long serialVersionUID = 12L;
	private String id;// 电影的ID
	private Long progressCount = (long) 0; // 总大小
	private Long currentProgress = (long) 0;// 当前进度
	private Integer downloadState = 0; // 下载状态
	private boolean editState;// 是否是编辑状态
	//private Bitmap movieHeadImage;
	private String movieHeadImagePath;//电影图片的路径
	private String fileSize;// 电影大小
	private String movieName;// 电影名称
	private String downloadUrl; // 下载地址
	private String setCount;// 为第几集
	private DownloadFile downloadFile; // 下载控制器
	private String percentage = "%0"; // 下载百分比的字符串
	private Long uuid; // 下载任务的标识
	private String filePath; // 存储路径
	private boolean isSelected; // 选中状态
	private String movieId;

//	private boolean existDwonloadQueue;//是否身在下载队列中
	public Long getProgressCount()
	{
		return progressCount;
	}

	public void setProgressCount(Long progressCount)
	{
		this.progressCount = progressCount;
	}

	public Long getCurrentProgress()
	{
		return currentProgress;
	}

	public void setCurrentProgress(Long currentProgress)
	{
		this.currentProgress = currentProgress;
	}

	public Integer getDownloadState()
	{
		return downloadState;
	}

	public void setDownloadState(Integer downloadState)
	{
		this.downloadState = downloadState;
	}

	public boolean isEditState()
	{
		return editState;
	}

	public void setEditState(boolean editState)
	{
		this.editState = editState;
	}

	/*public Bitmap getMovieHeadImage()
	{
		return movieHeadImage;
	}*/

	/*public void setMovieHeadImage(Bitmap movieHeadImage)
	{
		this.movieHeadImage = movieHeadImage;
	}*/

	public String getFileSize()
	{
		return fileSize;
	}

	public void setFileSize(String fileSize)
	{
		this.fileSize = fileSize;
	}

	public String getMovieName()
	{
		return movieName;
	}

	public void setMovieName(String movieName)
	{
		this.movieName = movieName;
	}

	public static long getSerialversionuid()
	{
		return serialVersionUID;
	}

	/*@Override
	public String toString()
	{
		return "DownloadMovieItem [progressCount=" + progressCount
				+ ", currentProgress=" + currentProgress + ", downloadState="
				+ downloadState + ", editState=" + editState
				+ ", movieHeadImage=" + movieHeadImage + ", fileSize="
				+ fileSize + ", movieName=" + movieName + "]";
	}*/
	@Override
	public String toString()
	{
		return "DownloadMovieItem [progressCount=" + progressCount
				+ ", currentProgress=" + currentProgress + ", downloadState="
				+ downloadState + ", editState=" + editState
				+ ", fileSize="
				+ fileSize + ", movieName=" + movieName + "]";
	}

	public String getDownloadUrl()
	{
		return downloadUrl;
	}

	public void setDownloadUrl(String downloadUrl)
	{
		this.downloadUrl = downloadUrl;
	}

	public DownloadFile getDownloadFile()
	{
		return downloadFile;
	}

	public void setDownloadFile(DownloadFile downloadFile)
	{
		this.downloadFile = downloadFile;
	}

	public String getPercentage()
	{
		return percentage;
	}

	public void setPercentage(String percentage)
	{
		this.percentage = percentage;
	}

	public Long getUuid()
	{
		return uuid;
	}

	public void setUuid(Long uuid)
	{
		this.uuid = uuid;
	}

	public String getFilePath()
	{
		return filePath;
	}

	public void setFilePath(String filePath)
	{
		this.filePath = filePath;
	}

	public boolean isSelected()
	{
		return isSelected;
	}

	public void setSelected(boolean isSelected)
	{
		this.isSelected = isSelected;
	}

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String getSetCount()
	{
		return setCount;
	}

	public void setSetCount(String setCount)
	{
		this.setCount = setCount;
	}

	public String getMovieHeadImagePath()
	{
		return movieHeadImagePath;
	}

	public void setMovieHeadImagePath(String movieHeadImagePath)
	{
		this.movieHeadImagePath = movieHeadImagePath;
	}

	public String getMovieId()
	{
		return movieId;
	}

	public void setMovieId(String movieId)
	{
		this.movieId = movieId;
	}

	

//	public boolean isExistDwonloadQueue()
//	{
//		return existDwonloadQueue;
//	}
//
//	public void setExistDwonloadQueue(boolean existDwonloadQueue)
//	{
//		this.existDwonloadQueue = existDwonloadQueue;
//	}

}
