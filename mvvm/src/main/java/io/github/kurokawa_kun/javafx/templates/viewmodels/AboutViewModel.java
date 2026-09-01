package io.github.kurokawa_kun.javafx.templates.viewmodels;
import io.github.kurokawa_kun.javafx.templates.*;
import io.github.kurokawa_kun.javafx.templates.entities.*;
import io.github.kurokawa_kun.javafx.templates.models.*;
import javafx.scene.image.Image;
import javafx.collections.*;
import javafx.beans.property.*;
import lombok.*;
import org.springframework.stereotype.Component;

/**
 *   About画面のビューモデル
 */
@Component
public class AboutViewModel
{
    private final AppInfo appInfo;
    
    private final ObjectProperty<Image> logo = new SimpleObjectProperty<>(this, "logo");
    private final StringProperty appName = new SimpleStringProperty(this, "appName");
    private final StringProperty appVersion = new SimpleStringProperty(this, "appVersion");
    private final StringProperty companyName = new SimpleStringProperty(this, "companyName");
    private final StringProperty contactInfo = new SimpleStringProperty(this, "contactInfo");
    
    @Getter
    private ObservableList<EnvironmentVariable> dataCmdLineArgs = FXCollections.observableArrayList();
    @Getter
    private ObservableList<EnvironmentVariable> dataRtmInfo = FXCollections.observableArrayList();
    @Getter
    private ObservableList<EnvironmentVariable> dataSysProps = FXCollections.observableArrayList();
    @Getter
    private ObservableList<EnvironmentVariable> dataEnvVars = FXCollections.observableArrayList();
    
    /**
     *   コンストラクタ
     *  @param appInfo アプリの共通情報
     *  @param systemInfoModel システム情報
     */    
    public AboutViewModel(AppInfo appInfo, SystemInfoModel systemInfoModel)
    {
        this.appInfo = appInfo;
        
        //  システム情報をリストに追加する
        //  システム情報が取得できない場合は(NONE)を表示する
        dataCmdLineArgs = FXCollections.observableArrayList(systemInfoModel.getSystemInfo().dataCmdLineArgs());
        if (dataCmdLineArgs.isEmpty()) dataCmdLineArgs.add(EnvironmentVariable.NONE);
        dataRtmInfo = FXCollections.observableArrayList(systemInfoModel.getSystemInfo().dataRtmInfo());
        if (dataRtmInfo.isEmpty()) dataRtmInfo.add(EnvironmentVariable.NONE);
        dataSysProps = FXCollections.observableArrayList(systemInfoModel.getSystemInfo().dataSysProps());
        if (dataSysProps.isEmpty()) dataSysProps.add(EnvironmentVariable.NONE);
        dataEnvVars = FXCollections.observableArrayList(systemInfoModel.getSystemInfo().dataEnvVars());
        if (dataEnvVars.isEmpty()) dataEnvVars.add(EnvironmentVariable.NONE);
    }
    
    /**
     *   画面の作成
     */
    public void initialize()
    {
        this.setLogo(appInfo.getLogo());
        this.setAppName(appInfo.getName());
        this.setAppVersion(appInfo.getVersion());
        this.setCompanyName(appInfo.getAuthorName());
        this.setContactInfo(String.join("\n", appInfo.getContactInfo()));
    }
    
    //  プロパティのアクセサ
    public Image getLogo()
    {
        return this.logo.get();
    }
    public void setLogo(Image image)
    {
        this.logo.set(image);
    }
    public ObjectProperty<Image> logoProperty()
    {
        return this.logo;
    }
        
    public String getAppName()
    {
        return this.appName.get();
    }
    public void setAppName(String appName)
    {
        this.appName.set(appName);
    }
    public StringProperty appNameProperty()
    {
        return this.appName;
    }
    
    public String getAppVersion()
    {
        return this.appVersion.get();
    }
    public void setAppVersion(String appVersion)
    {
        this.appVersion.set(appVersion);
    }
    public StringProperty appVersionProperty()
    {
        return this.appVersion;
    }
    
    public String getCompanyName()
    {
        return this.companyName.get();
    }
    public void setCompanyName(String companyName)
    {
        this.companyName.set(companyName);
    }
    public StringProperty companyNameProperty()
    {
        return this.companyName;
    }
    
    public String getContactInfo()
    {
        return this.contactInfo.get();
    }
    public void setContactInfo(String contactInfo)
    {
        this.contactInfo.set(contactInfo);
    }
    public StringProperty contactInfoProperty()
    {
        return this.contactInfo;
    }    
}
