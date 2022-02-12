package pinacolada.cards.eternal.normal;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.interfaces.subscribers.OnStartOfTurnPostDrawSubscriber;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.eternal.EternalCard;
import pinacolada.powers.EternalPower;
import pinacolada.powers.PCLCombatStats;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class PrayerOfTheMantis extends EternalCard implements OnStartOfTurnPostDrawSubscriber
{
    public static final PCLCardData DATA = Register(PrayerOfTheMantis.class).SetSkill(1, CardRarity.UNCOMMON);

    public PrayerOfTheMantis()
    {
        super(DATA);

        Initialize(0, 0, 1, 0);
        SetUpgrade(0, 0, 0, 0);
        SetCostUpgrade(-1);

        SetLight();
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        if (CheckPrimaryCondition(true)) {
            PCLActions.Bottom.StackPower(new PrayerOfTheMantisPower(player, magicNumber));
        }
        else {
            PCLCombatStats.onStartOfTurnPostDraw.SubscribeOnce(this);
        }
    }

    @Override
    public void OnStartOfTurnPostDraw()
    {
        super.OnStartOfTurnPostDraw();
        PCLActions.Bottom.StackPower(new PrayerOfTheMantisPower(player, magicNumber));
    }

    public static class PrayerOfTheMantisPower extends EternalPower
    {
        public PrayerOfTheMantisPower(AbstractCreature owner, int amount)
        {
            super(owner, PrayerOfTheMantis.DATA);

            Initialize(amount);
        }

        @Override
        public void updateDescription()
        {
            this.description = FormatDescription(0, amount);
        }

        @Override
        public void atEndOfTurn(boolean isPlayer)
        {
            super.atEndOfTurn(isPlayer);
            ReducePower(1);
        }

        public float atDamageGive(float damage, DamageInfo.DamageType type, AbstractCard card) {
            if (type == DamageInfo.DamageType.NORMAL && card != null && PCLGameUtilities.HasDarkAffinity(card) && card.baseDamage > 0) {
                damage *= 2;
            }

            return super.atDamageGive(damage, type, card);
        }
    }
}