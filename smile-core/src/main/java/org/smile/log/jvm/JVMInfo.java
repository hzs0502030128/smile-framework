package org.smile.log.jvm;
/**
 * 机器 信息
 * @author 胡真山
 */
public class JVMInfo {
    /** 可使用内存. */
    private long totalMemory;
    
    /**  剩余内存. */
    private long freeMemory;
    
    /** 最大可使用内存. */
    private long maxMemory;
    
    /** 操作系统. */
    private String osName;
    
    /** 总的物理内存. */
    private long totalMemorySize;
    
    /** 剩余的物理内存. */
    private long freePhysicalMemorySize;
    
    /** 已使用的物理内存. */
    private long usedMemory;
    
    /** 线程总数. */
    private int totalThread;
    
    /** cpu使用率. */
    private double cpuRatio;

    public long getJvmFreeMemory() {
        return freeMemory;
    }

    public void setJvmFreeMemory(long freeMemory) {
        this.freeMemory = freeMemory;
    }

    public long getFreePhysicalMemorySize() {
        return freePhysicalMemorySize;
    }

    public void setFreePhysicalMemorySize(long freePhysicalMemorySize) {
        this.freePhysicalMemorySize = freePhysicalMemorySize;
    }

    public long getMaxMemory() {
        return maxMemory;
    }

    public void setJvmMaxMemory(long maxMemory) {
        this.maxMemory = maxMemory;
    }

    public String getOsName() {
        return osName;
    }

    public void setOsName(String osName) {
        this.osName = osName;
    }

    public long getJvmTotalMemory() {
        return totalMemory;
    }

    public void setJvmTotalMemory(long totalMemory) {
        this.totalMemory = totalMemory;
    }

    public long getTotalPhysicalMemorySize() {
        return totalMemorySize;
    }

    public void setTotalPhysicalMemorySize(long totalMemorySize) {
        this.totalMemorySize = totalMemorySize;
    }

    public int getTotalThread() {
        return totalThread;
    }

    public void setTotalThread(int totalThread) {
        this.totalThread = totalThread;
    }

    public long getUsedPhysicalMemory() {
        return usedMemory;
    }

    public void setUsedPhysicalMemory(long usedMemory) {
        this.usedMemory = usedMemory;
    }

    public double getCpuRatio() {
        return cpuRatio;
    }

    public void setCpuRatio(double cpuRatio) {
        this.cpuRatio = cpuRatio;
    }
}
