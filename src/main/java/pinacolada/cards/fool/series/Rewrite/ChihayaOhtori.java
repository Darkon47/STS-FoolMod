package pinacolada.cards.fool.series.Rewrite;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.*;
import pinacolada.cards.base.cardeffects.GenericCardEffect;
import pinacolada.cards.base.modifiers.CostModifiers;
import pinacolada.cards.fool.FoolCard;
import pinacolada.effects.AttackEffects;
import pinacolada.powers.PCLPowerHelper;
import pinacolada.stances.pcl.EnduranceStance;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLJUtils;

public class ChihayaOhtori extends FoolCard
{
    public static final PCLCardData DATA = Register(ChihayaOhtori.class).SetAttack(2, CardRarity.UNCOMMON, PCLAttackType.Normal).SetSeriesFromClassPackage();

    public ChihayaOhtori()
    {
        super(DATA);

        Initialize(10, 0, 3, 2);
        SetUpgrade(3, 0, 0);
        SetAffinity_Green(1, 0, 1);
        SetAffinity_Orange(1, 0, 2);
    }

    @Override
    public void atTurnStart()
    {
        super.atTurnStart();

        Refresh(null);
    }

    @Override
    public void Refresh(AbstractMonster enemy)
    {
        super.Refresh(enemy);

        RefreshCost();
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.DealCardDamage(this, m, AttackEffects.SLASH_HEAVY);
        TrySetupChoices(new GenericCardEffect_Chihaya(this), GenericCardEffect.Gain(secondaryValue, PCLPowerHelper.TemporaryArtifact))
                .Select(1, m);
    }

    public void RefreshCost()
    {
        int orange = GetHandAffinity(PCLAffinity.Orange, false);
        if (EnduranceStance.IsActive()) {
            CostModifiers.For(this).Set(-1);
        }
        else {
            CostModifiers.For(this).Set(0);
        }
    }

    protected static class GenericCardEffect_Chihaya extends GenericCardEffect
    {
        private final ChihayaOhtori chihaya;

        public GenericCardEffect_Chihaya(ChihayaOhtori chihaya)
        {
            this.chihaya = chihaya;
        }

        @Override
        public String GetText()
        {
            return PCLJUtils.Format(ChihayaOhtori.DATA.Strings.EXTENDED_DESCRIPTION[0]);
        }

        @Override
        public void Use(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
        {
            PCLActions.Top.FetchFromPile(chihaya.name, 1, player.discardPile)
                    .SetOptions(false, false)
                    .SetFilter(c -> (c instanceof PCLCard && ((PCLCard) c).affinities.GetLevel(PCLAffinity.Orange) > 0 && !chihaya.cardID.equals(c.cardID)))
                    .AddCallback(cards ->
                    {
                        for (AbstractCard card2 : cards) {
                            PCLActions.Bottom.Motivate(card2, 1);
                        }
                    });
        }
    }

}