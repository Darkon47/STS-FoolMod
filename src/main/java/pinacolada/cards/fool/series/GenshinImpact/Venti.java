package pinacolada.cards.fool.series.GenshinImpact;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import eatyourbeets.interfaces.subscribers.OnOrbPassiveEffectSubscriber;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.PCLCardTarget;
import pinacolada.cards.base.PCLCardTrait;
import pinacolada.cards.fool.FoolCard;
import pinacolada.orbs.pcl.Air;
import pinacolada.powers.FoolPower;
import pinacolada.powers.PCLCombatStats;
import pinacolada.powers.common.SupportDamagePower;
import pinacolada.powers.fool.SwirledPower;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class Venti extends FoolCard
{
    public static final PCLCardData DATA = Register(Venti.class)
            .SetSkill(2, CardRarity.RARE, PCLCardTarget.None)
            .SetMaxCopies(2)
            .SetMultiformData(2, false)
            .SetTraits(PCLCardTrait.Fairy)
            .SetSeriesFromClassPackage();

    public Venti()
    {
        super(DATA);

        Initialize(0, 1, 2, 1);
        SetAffinity_Star(1, 0, 0);
        SetAffinity_Green(0,0,2);
        SetAffinity_Light(0, 0, 1);

        SetEthereal(true);
    }

    @Override
    protected String GetRawDescription(Object... args)
    {
        return super.GetRawDescription(cardData.Strings.EXTENDED_DESCRIPTION[auxiliaryData.form + 1]);
    }

    @Override
    public int SetForm(Integer form, int timesUpgraded) {
        if (timesUpgraded > 0) {
            if (form == 1) {
                SetUpgrade(0, 0, 0, 0);
            }
            else {
                SetUpgrade(0, 0, 1, 0);
            }
        }
        return super.SetForm(form, timesUpgraded);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        

        AbstractOrb orb = new Air();
        PCLActions.Bottom.ChannelOrb(orb);
        PCLActions.Bottom.StackPower(new VentiPower(player, 1));

        PCLActions.Bottom.Cycle(name, magicNumber).SetOptions(true, true, true).AddCallback(cards ->
        {
            for (AbstractCard card : cards)
            {
                if (card.type == CardType.SKILL)
                {
                    PCLActions.Bottom.TriggerOrbPassive(auxiliaryData.form == 1 && upgraded ? player.orbs.size() : 1).SetFilter(o -> Air.ORB_ID.equals(o.ID));
                }
                else {
                    PCLActions.Bottom.StackPower(new SupportDamagePower(player, secondaryValue));
                }
            }
        });

    }
    public static class VentiPower extends FoolPower implements OnOrbPassiveEffectSubscriber
    {
        public VentiPower(AbstractCreature owner, int amount)
        {
            super(owner, Venti.DATA);

            Initialize(amount);
        }

        @Override
        public void updateDescription()
        {
            this.description = FormatDescription(0, amount);
        }

        @Override
        public void onInitialApplication()
        {
            super.onInitialApplication();

            PCLCombatStats.onOrbPassiveEffect.Subscribe(this);
        }

        @Override
        public void onRemove()
        {
            super.onRemove();

            PCLCombatStats.onOrbPassiveEffect.Unsubscribe(this);
        }

        @Override
        public void OnOrbPassiveEffect(AbstractOrb orb) {
            if (Air.ORB_ID.equals(orb.ID)) {
                for (AbstractMonster m : PCLGameUtilities.GetEnemies(true)) {
                    PCLActions.Delayed.StackPower(player, new SwirledPower(m, 1));
                }
                flash();
            }
        }

        @Override
        public void onEvokeOrb(AbstractOrb orb)
        {
            super.onEvokeOrb(orb);

            if (Air.ORB_ID.equals(orb.ID)) {
                for (AbstractMonster m : PCLGameUtilities.GetEnemies(true)) {
                    PCLActions.Delayed.StackPower(player, new SwirledPower(m, 1));
                }
                flash();
            }
        }

        @Override
        public void atEndOfTurn(boolean isPlayer)
        {
            super.atEndOfTurn(isPlayer);
            ReducePower(1);
        }
    }

}