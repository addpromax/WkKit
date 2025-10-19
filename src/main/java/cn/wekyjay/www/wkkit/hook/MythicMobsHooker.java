package cn.wekyjay.www.wkkit.hook;

import cn.wekyjay.www.wkkit.WkKit;
import io.lumine.mythic.api.MythicProvider;
import io.lumine.mythic.bukkit.BukkitAdapter;
import io.lumine.mythic.core.mobs.ActiveMob;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class MythicMobsHooker {
    public static MythicMobsHooker mythicMobsHooker;
    public MythicMobsHooker(){
        WkKit.getWkKit().getLogger().info("§7已成功与 §6MythicMobs §7挂钩服务.");
    }

    /**
     * 获取MM怪的Hooker
     * @return MythicMobsHooker
     */
    public static MythicMobsHooker getMythicMobs(){
        return mythicMobsHooker == null?mythicMobsHooker = new MythicMobsHooker():mythicMobsHooker;
    }

    /**
     * 指定MM怪名称在玩家Location生成Mob
     * @param player 玩家
     * @param mobName 怪物名称
     * @return 布尔值(是否生成成功)
     */
    public boolean spawnMob(Player player, String mobName){
        try {
            io.lumine.mythic.api.mobs.MobManager mobManager = MythicProvider.get().getMobManager();
            Location spawnLocation = player.getLocation();
            // 生成Mob
            ActiveMob activeMob = mobManager.getMythicMob(mobName)
                    .map(mob -> mob.spawn(BukkitAdapter.adapt(spawnLocation), 1))
                    .orElse(null);
            if (activeMob != null) {
                // 将mob转化为bukkit生物
                Entity entity = activeMob.getEntity().getBukkitEntity();
                return true;
            }
        } catch (Exception e) {
            WkKit.getWkKit().getLogger().warning("生成MythicMobs怪物失败: " + e.getMessage());
        }
        return false;
    }
}
