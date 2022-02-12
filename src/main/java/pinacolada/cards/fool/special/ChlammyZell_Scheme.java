package pinacolada.cards.fool.special;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.FlickCoinEffect;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.PCLCardTarget;
import pinacolada.cards.fool.FoolCard;
import pinacolada.cards.fool.series.NoGameNoLife.ChlammyZell;
import pinacolada.effects.AttackEffects;
import pinacolada.powers.FoolPower;
import pinacolada.utilities.PCLActions;

public class ChlammyZell_Scheme extends FoolCard
{
    public static final PCLCardData DATA = Register(ChlammyZell_Scheme.class)
            .SetSkill(1, CardRarity.SPECIAL, PCLCardTarget.None)
            .SetSeries(ChlammyZell.DATA.Series);

    public ChlammyZell_Scheme()
    {
        super(DATA);

        Initialize(0, 0, 5);
        SetCostUpgrade(-1);

        SetAffinity_Blue(1);
        SetAffinity_Dark(1);

        SetRetain(true);
        SetExhaust(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.VFX(new FlickCoinEffect(p.hb.cX, p.hb.cY, p.hb.cX, p.hb.cY + p.hb.height), 0.15f);
        PCLActions.Bottom.StackPower(new ChlammyZellPower(p, magicNumber));
    }

    public static class ChlammyZellPower extends FoolPower
    {
        private AbstractCard.CardType lastType;

        public ChlammyZellPower(AbstractCreature owner, int amount)
        {
            super(owner, ChlammyZell_Scheme.DATA);

            lastType = AbstractCard.CardType.SKILL;

            Initialize(amount);
        }

        @Override
        public void updateDescription()
        {
            this.description = FormatDescription(0, amount, lastType);
        }

        @Override
        public void onAfterCardPlayed(AbstractCard card)
        {
            super.onAfterCardPlayed(card);

            if (card.type != lastType)
            {
                lastType = card.type;

                final int[] damage = DamageInfo.createDamageMatrix(amount, true);
                PCLActions.Bottom.DealDamageToAll(damage, DamageInfo.DamageType.THORNS, AttackEffects.SLASH_HORIZONTAL);
                PCLActions.Bottom.Cycle(name, 1);

                updateDescription();
            }
        }

        @Override
        public void atEndOfTurn(boolean isPlayer)
        {
            super.atEndOfTurn(isPlayer);

            RemovePower();
        }
    }
}