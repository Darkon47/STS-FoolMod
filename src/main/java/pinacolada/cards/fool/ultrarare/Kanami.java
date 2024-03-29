package pinacolada.cards.fool.ultrarare;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.WhirlwindEffect;
import eatyourbeets.utilities.CardSelection;
import pinacolada.cards.base.*;
import pinacolada.cards.fool.FoolCard_UltraRare;
import pinacolada.effects.AttackEffects;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameEffects;

// TODO Rewrite to use alt forms
public class Kanami extends FoolCard_UltraRare
{
    public static final PCLCardData DATA = Register(Kanami.class)
            .SetAttack(2, CardRarity.SPECIAL, PCLAttackType.Normal, PCLCardTarget.AoE)
            .SetColorless()
            .SetSeries(CardSeries.LogHorizon)
            .PostInitialize(data -> data.AddPreview(new KanamiAlt(), true));

    public Kanami()
    {
        super(DATA);

        Initialize(20, 0, 2);
        SetUpgrade(7, 0, 0);

        SetAffinity_Red(1);
        SetAffinity_Green(1, 0, 2);
        SetAffinity_Light(1);

        SetCooldown(2, 0, this::OnCooldownCompleted);
        SetHaste(true);
    }

    @Override
    public int SetForm(Integer form, int timesUpgraded) {
        if (form == 1) {
            magicNumber = 10;
        }
        else {
            magicNumber = 2;
        }
        return super.SetForm(form, timesUpgraded);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.DealCardDamageToAll(this, AttackEffects.SLASH_HORIZONTAL)
                .forEach(d -> d.SetVFX(true, false)
        .AddCallback(enemies ->
        {
            CardCrawlGame.sound.play("ATTACK_WHIRLWIND");
            for (AbstractCreature c : enemies)
            {
                PCLActions.Top.VFX(new WhirlwindEffect(), 0);
                PCLGameEffects.List.Add(new WhirlwindEffect());
                PCLActions.Bottom.ApplyVulnerable(player, c, magicNumber)
                .ShowEffect(false, true);
            }
        }));
        PCLActions.Last.MoveCard(this, p.drawPile)
        .ShowEffect(true, true)
        .SetDestination(CardSelection.Random)
        .AddCallback(() -> cooldown.ProgressCooldownAndTrigger(null));
    }

    private void OnCooldownCompleted(AbstractMonster m)
    {
        PCLActions.Last.ReplaceCard(uuid, new KanamiAlt())
        .SetUpgrade(upgraded)
        .AddCallback(cardMap ->
        {
            for (AbstractCard key : cardMap.keySet())
            {
                ((KanamiAlt) cardMap.get(key)).SetScaling(affinities);
            }
        });
    }
}