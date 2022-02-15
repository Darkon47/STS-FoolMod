package pinacolada.cards.fool.colorless;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.potions.PotionSlot;
import com.megacrit.cardcrawl.powers.PoisonPower;
import eatyourbeets.powers.CombatStats;
import pinacolada.cards.base.CardSeries;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.PCLCardTarget;
import pinacolada.cards.fool.FoolCard;
import pinacolada.powers.common.BurningPower;
import pinacolada.powers.common.DelayedDamagePower;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLJUtils;

public class EirinYagokoro extends FoolCard
{
    public static final PCLCardData DATA = Register(EirinYagokoro.class)
            .SetSkill(1, CardRarity.UNCOMMON, PCLCardTarget.Normal)
            .SetColorless()
            .SetMaxCopies(2)
            .SetSeries(CardSeries.TouhouProject);

    public EirinYagokoro()
    {
        super(DATA);

        Initialize(0, 1, 2, 5);
        SetUpgrade(0, 0, 0, 0);
        SetAffinity_Blue(1, 0 ,1);
        SetAffinity_Light(1, 0 ,1);
        SetExhaust(true);

        SetHealing(true);
    }

    @Override
    public void Refresh(AbstractMonster enemy)
    {
        super.Refresh(enemy);

        heal = (CombatStats.CanActivateLimited(cardID) && PCLJUtils.Find(player.potions, potion -> !(potion instanceof PotionSlot) && potion.canUse()) == null) ? secondaryValue : 0;
    }

    @Override
    public void OnUpgrade() {
        super.OnUpgrade();
        SetRetainOnce(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.GainBlock(block);
        PCLActions.Bottom.RemovePower(player, player, PoisonPower.POWER_ID);
        PCLActions.Bottom.RemovePower(player, player, BurningPower.POWER_ID);
        if (upgraded) {
            PCLActions.Bottom.RemovePower(player, player, DelayedDamagePower.POWER_ID);
        }

        if (info.TryActivateLimited()) {

            boolean hasUsed = false;
            for (AbstractPotion potion : player.potions) {
                if (!(potion instanceof PotionSlot) && potion.canUse()) {
                    PCLActions.Bottom.UsePotion(potion, m).SetShouldRemove(false);
                    PCLActions.Bottom.UsePotion(potion, m);
                    hasUsed = true;
                    break;
                }
            }
            if (!hasUsed) {
                PCLActions.Bottom.Heal(heal);
            }
        }
    }
}

