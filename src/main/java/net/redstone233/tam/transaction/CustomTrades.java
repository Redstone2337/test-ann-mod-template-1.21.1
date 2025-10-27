package net.redstone233.tam.transaction;

import net.fabricmc.fabric.api.object.builder.v1.trade.TradeOfferHelper;
import net.minecraft.item.Items;
import net.minecraft.village.TradeOffers;
import net.minecraft.village.VillagerProfession;
import net.redstone233.tam.TestAnnMod;
import net.redstone233.tam.item.ModItems;

public class CustomTrades {
    public static void init() {
        // TODO: Add custom trades
        TradeOfferHelper.registerVillagerOffers(
                VillagerProfession.TOOLSMITH, 2, factories -> {
                    factories.add(new TradeOffers.BuyItemFactory(ModItems.SUSPICIOUS_SUBSTANCE,16,15,5,10));
                    factories.add(new TradeOffers.SellItemFactory(ModItems.SUSPICIOUS_SUBSTANCE,10,16,15,2,0.5f));
                }
        );

        TradeOfferHelper.registerVillagerOffers(
                VillagerProfession.WEAPONSMITH, 5, factories -> {
                    factories.add(new TradeOffers.BuyItemFactory(ModItems.BLAZING_FLAME_SWORD,300,1,1,1));
                    factories.add(new TradeOffers.BuyItemFactory(ModItems.ICE_FREEZE_SWORD,300,1,1,1));
                }
        );

        TradeOfferHelper.registerVillagerOffers(
                VillagerProfession.ARMORER, 2, factories -> {
                    factories.add(new TradeOffers.ProcessItemFactory(ModItems.GUIDITE_HELMET, 1, 2, ModItems.SUSPICIOUS_SUBSTANCE, 5, 16, 1, 0.5f));
                    factories.add(new TradeOffers.ProcessItemFactory(ModItems.GUIDITE_CHESTPLATE, 1, 2, ModItems.SUSPICIOUS_SUBSTANCE, 8, 16, 1, 0.5f));
                }
        );

        TradeOfferHelper.registerVillagerOffers(
                VillagerProfession.ARMORER, 3, factories -> {
                    factories.add(new TradeOffers.ProcessItemFactory(ModItems.GUIDITE_LEGGINGS, 1, 2, ModItems.SUSPICIOUS_SUBSTANCE, 7, 16, 1, 0.5f));
                    factories.add(new TradeOffers.ProcessItemFactory(ModItems.GUIDITE_BOOTS, 1, 2, ModItems.SUSPICIOUS_SUBSTANCE, 4, 16, 1, 0.5f));
                }
        );

        TestAnnMod.LOGGER.info("Custom trades registered!");
    }
}
