package iot.dashboard;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.influxdb.annotations.Column;
import com.influxdb.annotations.Measurement;

@Measurement(name="air_quality")
public class AirQualityData {
    @Column
    @JsonProperty("Date")
    private String date;
    @Column
    @JsonProperty("Average_CO")
    private float averageCO;
    @Column
    @JsonProperty("Average_PT08S1_CO")
    private float averagePT08S1CO;
    @Column
    @JsonProperty("Average_NMHC_GT")
    private float averageNMHCGT;
    @Column
    @JsonProperty("Average_C6H6_GT")
    private float averageC6H6GT;
    @Column
    @JsonProperty("Average_PT08S2_NMHC")
    private float averagePT08S2NMHC;
    @Column
    @JsonProperty("Average_NOx_GT")
    private float averageNOxGT;
    @Column
    @JsonProperty("Average_PT08S3_NOx")
    private float averagePT08S3NOx;
    @Column
    @JsonProperty("Average_NO2_GT")
    private float averageNO2GT;
    @Column
    @JsonProperty("Average_PT08S4_NO2")
    private float averagePT08S4NO2;
    @Column
    @JsonProperty("Average_PT08S5_O3")
    private float averagePT08S5O3;
    @Column
    @JsonProperty("Average_Temperature")
    private float averageTemperature;
    @Column
    @JsonProperty("Average_RelativeHumidity")
    private float averageRelativeHumidity;
    @Column
    @JsonProperty("Average_AbsoluteHumidity")
    private float averageAbsoluteHumidity;
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public float getAverageCO() {
        return averageCO;
    }

    public void setAverageCO(float averageCO) {
        this.averageCO = averageCO;
    }

    public float getAveragePT08S1CO() {
        return averagePT08S1CO;
    }

    public void setAveragePT08S1CO(float averagePT08S1CO) {
        this.averagePT08S1CO = averagePT08S1CO;
    }

    public float getAverageNMHCGT() {
        return averageNMHCGT;
    }

    public void setAverageNMHCGT(float averageNMHCGT) {
        this.averageNMHCGT = averageNMHCGT;
    }

    public float getAverageC6H6GT() {
        return averageC6H6GT;
    }

    public void setAverageC6H6GT(float averageC6H6GT) {
        this.averageC6H6GT = averageC6H6GT;
    }

    public float getAveragePT08S2NMHC() {
        return averagePT08S2NMHC;
    }

    public void setAveragePT08S2NMHC(float averagePT08S2NMHC) {
        this.averagePT08S2NMHC = averagePT08S2NMHC;
    }

    public float getAverageNOxGT() {
        return averageNOxGT;
    }

    public void setAverageNOxGT(float averageNOxGT) {
        this.averageNOxGT = averageNOxGT;
    }

    public float getAveragePT08S3NOx() {
        return averagePT08S3NOx;
    }

    public void setAveragePT08S3NOx(float averagePT08S3NOx) {
        this.averagePT08S3NOx = averagePT08S3NOx;
    }

    public float getAverageNO2GT() {
        return averageNO2GT;
    }

    public void setAverageNO2GT(float averageNO2GT) {
        this.averageNO2GT = averageNO2GT;
    }

    public float getAveragePT08S4NO2() {
        return averagePT08S4NO2;
    }

    public void setAveragePT08S4NO2(float averagePT08S4NO2) {
        this.averagePT08S4NO2 = averagePT08S4NO2;
    }

    public float getAveragePT08S5O3() {
        return averagePT08S5O3;
    }

    public void setAveragePT08S5O3(float averagePT08S5O3) {
        this.averagePT08S5O3 = averagePT08S5O3;
    }

    public float getAverageTemperature() {
        return averageTemperature;
    }

    public void setAverageTemperature(float averageTemperature) {
        this.averageTemperature = averageTemperature;
    }

    public float getAverageRelativeHumidity() {
        return averageRelativeHumidity;
    }

    public void setAverageRelativeHumidity(float averageRelativeHumidity) {
        this.averageRelativeHumidity = averageRelativeHumidity;
    }

    public float getAverageAbsoluteHumidity() {
        return averageAbsoluteHumidity;
    }

    public void setAverageAbsoluteHumidity(float averageAbsoluteHumidity) {
        this.averageAbsoluteHumidity = averageAbsoluteHumidity;
    }


}
