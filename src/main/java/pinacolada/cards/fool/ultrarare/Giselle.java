package pinacolada.cards.fool.ultrarare;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.FireBreathingPower;
import com.megacrit.cardcrawl.vfx.combat.FlameBarrierEffect;
import eatyourbeets.utilities.TargetHelper;
import pinacolada.cards.base.*;
import pinacolada.cards.fool.FoolCard_UltraRare;
import pinacolada.cards.fool.special.OrbCore_Fire;
import pinacolada.powers.PCLCombatStats;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameEffects;

public class Giselle extends FoolCard_UltraRare
{
    public static final PCLCardData DATA = Register(Giselle.class)
            .SetSkill(2, CardRarity.SPECIAL, PCLCardTarget.None)
            .SetColorless()
            .SetSeries(CardSeries.GATE);

    public Giselle()
    {
        super(DATA);

        Initialize(0, 0, 4);
        SetUpgrade(0, 0, 2);

        SetAffinity_Red(1);
        SetAffinity_Dark(1);

        SetExhaust(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.VFX(new FlameBarrierEffect(p.hb.cX, p.hb.cY), 0.5f);
        PCLActions.Bottom.TryChooseSpendAffinity(name, PCLCombatStats.MatchingSystem.GetAffinityLevel(PCLAffinity.General, true)).SetOptions(true, false).AddConditionalCallback(
                cards -> {
                    if (cards.size() > 0) {
                        PCLActions.Bottom.StackPower(new FireBreathingPower(player, cards.get(0).magicNumber));
                    }
                }
        );
    }

    @Override
    public void triggerOnEndOfTurnForPlayingCard()
    {
        super.triggerOnEndOfTurnForPlayingCard();
        PCLActions.Bottom.ApplyBurning(TargetHelper.Enemies(), magicNumber);
    }

    @Override
    public void triggerWhenCreated(boolean startOfBattle)
    {
        super.triggerWhenCreated(startOfBattle);

        if (startOfBattle)
        {
            PCLGameEffects.List.ShowCopy(this);
            AbstractCard c = new OrbCore_Fire();
            c.applyPowers();
            c.use(player, null);
        }
    }
}