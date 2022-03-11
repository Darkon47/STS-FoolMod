package pinacolada.cards.pcl.curse;

import com.evacipated.cardcrawl.mod.stslib.cards.interfaces.StartupCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.PCLCardTarget;
import pinacolada.effects.AttackEffects;
import pinacolada.effects.vfx.megacritCopy.HemokinesisEffect2;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameEffects;

public class Curse_Parasite extends PCLCard implements StartupCard
{
    public static final PCLCardData DATA = Register(Curse_Parasite.class)
            .SetCurse(-2, PCLCardTarget.None, false, true);

    public Curse_Parasite()
    {
        super(DATA);

        Initialize(0, 0, 2, 2);
        SetAffinity_Dark(1);

        SetUnplayable(true);
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();
        DoEffect();
    }

    @Override
    public void triggerOnPurge()
    {
        super.triggerOnPurge();
        DoEffect();
    }

    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
    }

    @Override
    public boolean atBattleStartPreDraw() {
        PCLActions.Bottom.DealDamageAtEndOfTurn(player, player, magicNumber, AttackEffects.POISON);
        return true;
    }

    protected void DoEffect() {
        PCLActions.Bottom.DealDamageToRandomEnemy(secondaryValue, DamageInfo.DamageType.HP_LOSS, AttackEffects.NONE)
                .SetDamageEffect(enemy ->
                {
                    PCLGameEffects.List.Add(new HemokinesisEffect2(enemy.hb.cX, enemy.hb.cY, player.hb.cX, player.hb.cY));
                    return 0f;
                });
        PCLActions.Bottom.GainTemporaryHP(secondaryValue);
    }
}